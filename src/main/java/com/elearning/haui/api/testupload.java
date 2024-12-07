package com.elearning.haui.api;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.utils.UploadCloudinary;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/upload")
@RestController
@RequiredArgsConstructor
public class testupload {
    private final UploadCloudinary uploadCloudinary;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file)
            throws IOException {
        String url = uploadCloudinary.uploadImage(file);
        return ResponseEntity.ok().body(new RestResponse<>(
                HttpStatus.OK.value(),
                null,
                "Upload image successful",
                url));

    }
}
