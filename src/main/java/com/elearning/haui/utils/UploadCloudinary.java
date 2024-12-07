package com.elearning.haui.utils;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadCloudinary {
    String uploadImage(MultipartFile file) throws IOException;
}