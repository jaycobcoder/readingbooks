package com.readingbooks.web.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadUtilImpl implements ImageUploadUtil{

    @Override
    public String uploadImage(MultipartFile file) {
        String fileName = createFileName();
        String fileExtension = extractExtension(file.getOriginalFilename());
        String filePath = getFilePath(UPLOAD_PATH, fileName, fileExtension);

        File saveFile = new File(filePath);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName+"."+fileExtension;
    }

    @Override
    public String updateImage(MultipartFile file, String existingImageName) {
        deleteImage(existingImageName);

        return uploadImage(file);
    }

    @Override
    public void deleteImage(String savedImageName) {
        File existFile = new File(savedImageName);
        existFile.delete();
    }

    private String createFileName() {
        return UUID.randomUUID().toString();
    }


    private String extractExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    private String getFilePath(String uploadPath, String fileName, String fileExtension) {
        return uploadPath + fileName + "." + fileExtension;
    }
}
