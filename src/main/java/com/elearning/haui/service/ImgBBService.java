package com.elearning.haui.service;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.entity.ImageStorage;
import com.elearning.haui.repository.ImageStorageRepository;
import com.elearning.haui.service.imp.ImgBBServiceImp;

@Service
public class ImgBBService implements ImgBBServiceImp {

    private static final Logger log = LoggerFactory.getLogger(ImgBBService.class);
    @Autowired
    ImageStorageRepository imageStorageRepository;
    @Value("${imgbb.api.url}")
    private String imgbbUrl;

    @Value("${imgbb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String checkAndUploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            // Tính hash của ảnh
            String imageHash = calculateImageHash(file);

            // Kiểm tra đã tồn tại trong DB chưa
            Optional<ImageStorage> existing = imageStorageRepository.findByHash(imageHash);
            if (existing.isPresent()) {
                return existing.get().getUrl();
            }
            String uploadedUrl = upLoadImage(file);

            // Lưu vào DB
            ImageStorage newImage = new ImageStorage();
            newImage.setHash(imageHash);
            newImage.setUrl(uploadedUrl);
            imageStorageRepository.save(newImage);

            return uploadedUrl;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý ảnh: " + e.getMessage());
        }
    }


    private String calculateImageHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(file.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String upLoadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("File rỗng hoặc null");
            throw new IllegalArgumentException("File không được rỗng hoặc null");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", apiKey);
        try {
            body.add("image", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(imgbbUrl, HttpMethod.POST, requestEntity, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map data = (Map) response.getBody().get("data");
                String url = (String) data.get("url");
                log.info("Upload ảnh thành công, URL: {}", url);
                return url;
            } else {
                log.error("Upload ảnh thất bại, mã trạng thái: {}", response.getStatusCode());
                throw new RuntimeException("Không thể upload ảnh lên ImgBB: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Lỗi khi upload ảnh: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Lỗi khi upload ảnh: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Lỗi không xác định khi upload ảnh: {}", e.getMessage());
            throw new RuntimeException("Lỗi không xác định khi upload ảnh: " + e.getMessage(), e);
        }
    }


    @Override
    public boolean isSameImage(String imageUrl, String newImageBase64) {
        try {
            ResponseEntity<byte[]> imgResponse = restTemplate.getForEntity(imageUrl, byte[].class);
            if (imgResponse.getStatusCode().is2xxSuccessful()) {
                String existingImageBase64 = Base64.getEncoder().encodeToString(imgResponse.getBody());
                return existingImageBase64.equals(newImageBase64);
            }
        } catch (Exception e) {
            log.error("Lỗi khi so sánh ảnh: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteImage(String imageUrl) {
        try {
            String imageId = extractImageId(imageUrl);
            if (imageId == null) {

                return false;
            }

            String deleteUrl = "https://api.imgbb.com/1/image/" + imageId + "?key=" + apiKey;
            ResponseEntity<Map> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Map.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {

            return false;
        }
    }

    private String extractImageId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("/")) {
            return null;
        }
        String[] parts = imageUrl.split("/");
        return parts.length > 4 ? parts[parts.length - 1].split("\\.")[0] : null;
    }
}
