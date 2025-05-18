package com.elearning.haui.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.elearning.haui.domain.dto.UploadResultDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CloudinaryService {

    @Autowired
    @Qualifier("cloudinaryDinhLuong")
    private Cloudinary cloudinary;

    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    // Tạo SHA-256 hash từ nội dung file
    private String generateFileHash(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream())) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            byte[] hashBytes = digest.digest();
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new IOException("Không thể tạo hash cho file", e);
        }
    }

    @Async
    public CompletableFuture<UploadResultDTO> uploadFileIfNotExist(MultipartFile file, String resourceType, String publicId) throws IOException {
        if (file == null || file.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        // Log tên thread để kiểm tra chạy song song
        System.out.println("Thread " + Thread.currentThread().getName() + " - Đang xử lý upload file kiểu: " + resourceType + ", tên file: " + file.getOriginalFilename());

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("Kích thước file vượt quá giới hạn 100MB");
        }

        long startTime = System.nanoTime();

        // Tạo hash file
        long startHashTime = System.nanoTime();
        String fileHash = generateFileHash(file);
        long endHashTime = System.nanoTime();
        System.out.println("Thread " + Thread.currentThread().getName() + " - Thời gian tạo hash: " + (endHashTime - startHashTime) / 1_000_000 + " ms");

        // Tìm kiếm file trên Cloudinary dựa trên hash metadata
        long startSearchTime = System.nanoTime();
        try {
            Map searchResult = cloudinary.search()
                    .expression("context.file_hash=" + fileHash)
                    .maxResults(1)
                    .execute();

            long endSearchTime = System.nanoTime();
            System.out.println("Thread " + Thread.currentThread().getName() + " - Thời gian tìm kiếm hash trên Cloudinary: " + (endSearchTime - startSearchTime) / 1_000_000 + " ms");

            if (searchResult.containsKey("resources") && !((java.util.List<?>) searchResult.get("resources")).isEmpty()) {
                Map resource = (Map) ((java.util.List<?>) searchResult.get("resources")).get(0);
                String url = (String) resource.get("secure_url");

                Double duration = null;
                if ("video".equals(resourceType) && resource.containsKey("duration")) {
                    Object durationObj = resource.get("duration");
                    if (durationObj instanceof Number) {
                        duration = ((Number) durationObj).doubleValue();
                    }
                }

                long endTime = System.nanoTime();
                System.out.println("Thread " + Thread.currentThread().getName() + " - Tổng thời gian (file đã tồn tại): " + (endTime - startTime) / 1_000_000 + " ms");
                return CompletableFuture.completedFuture(new UploadResultDTO(url, duration));
            }
        } catch (Exception e) {
            System.out.println("Thread " + Thread.currentThread().getName() + " - Lỗi khi tìm kiếm file trên Cloudinary: " + e.getMessage());
        }

        // Upload file nếu không tìm thấy file tương ứng
        long startUploadTime = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream())) {
            Map uploadResult = cloudinary.uploader().uploadLarge(
                    bis,
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "public_id", publicId,
                            "overwrite", false,
                            "chunk_size", 100 * 1024 * 1024,
                            "context", "file_hash=" + fileHash // metadata
                    )
            );

            long endUploadTime = System.nanoTime();
            System.out.println("Thread " + Thread.currentThread().getName() + " - Thời gian upload lên Cloudinary: " + (endUploadTime - startUploadTime) / 1_000_000 + " ms");

            String url = (String) uploadResult.get("secure_url");

            Double duration = null;
            if ("video".equals(resourceType) && uploadResult.containsKey("duration")) {
                Object durationObj = uploadResult.get("duration");
                if (durationObj instanceof Number) {
                    duration = ((Number) durationObj).doubleValue();
                }
            }

            long endTime = System.nanoTime();
            System.out.println("Thread " + Thread.currentThread().getName() + " - Tổng thời gian (file mới): " + (endTime - startTime) / 1_000_000 + " ms");
            return CompletableFuture.completedFuture(new UploadResultDTO(url, duration));
        }
    }

    @Async
    public CompletableFuture<UploadResultDTO> uploadVideo(MultipartFile videoFile, String publicId) throws IOException {
        return uploadFileIfNotExist(videoFile, "video", publicId);
    }

    @Async
    public CompletableFuture<UploadResultDTO> uploadPdf(MultipartFile pdfFile, String publicId) throws IOException {
        return uploadFileIfNotExist(pdfFile, "raw", publicId);
    }
}
