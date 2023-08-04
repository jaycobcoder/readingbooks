package com.readingbooks.web.service.utils;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUtil {
    String upload(MultipartFile file);

    String update(MultipartFile file, String existingImageName);

    void delete(String savedImageName);
}
