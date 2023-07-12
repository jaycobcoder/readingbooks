package com.readingbooks.web.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadUtilImpl implements ImageUploadUtil{

    /**
     * 이미지 업로드 메소드
     * @param file
     * @return 저장된 이미지 이름 + .확장자명
     */
    @Override
    public String uploadImage(MultipartFile file) {
        String filename = createFileName();
        String fileExtension = extractExtension(file.getOriginalFilename());
        String filePath = getFilePath(UPLOAD_PATH, filename, fileExtension);

        File saveFile = new File(filePath);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filename+"."+fileExtension;
    }

    /**
     * 이미지 수정 메소드
     * @param file
     * @param existingImageName
     * @return 저장된 이미지 이름 + .확장자명
     */
    @Override
    public String updateImage(MultipartFile file, String existingImageName) {
        String fileExtension = extractExtension(existingImageName);
        String filename = extractFilename(existingImageName);
        String filePath = getFilePath(UPLOAD_PATH, filename, fileExtension);

        deleteImage(filePath);

        return uploadImage(file);
    }

    private String extractFilename(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }

    @Override
    public void deleteImage(String savedImageName) {
        File existFile = new File(savedImageName);
        existFile.delete();
    }

    private String createFileName() {
        return UUID.randomUUID().toString();
    }


    private String extractExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }

    private String getFilePath(String uploadPath, String filename, String fileExtension) {
        return uploadPath + filename + "." + fileExtension;
    }
}
