package com.elearning.haui.service.imp;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface ImgBBServiceImp {
    String upLoadImage(MultipartFile file);
    boolean isSameImage(String imageUrl, String newImageBase64);
    boolean deleteImage(String imageUrl);
}
