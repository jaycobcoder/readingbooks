package com.readingbooks.web.service.utils;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUtil {
    String UPLOAD_PATH = "D:/file/";

    String uploadImage(MultipartFile file);

    String updateImage(MultipartFile file, String existingImageName);

    void deleteImage( String savedImageName);
}
