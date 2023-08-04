package com.readingbooks.web.service.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageUploadUtilImpl implements ImageUploadUtil{
    private final String UPLOAD_PATH = "D:/file/";

    /**
     * 이미지 업로드 메소드
     * @param file
     * @return 저장된 이미지 이름 + .확장자명
     */
    @Override
    public String upload(MultipartFile file) {
        String filename = createFilename();
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
    public String update(MultipartFile file, String existingImageName) {
        delete(existingImageName);

        return upload(file);
    }

    private String extractFilename(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }

    @Override
    public void delete(String savedImageName) {
        String fileExtension = extractExtension(savedImageName);
        String filename = extractFilename(savedImageName);
        String filePath = getFilePath(UPLOAD_PATH, filename, fileExtension);

        File existFile = new File(filePath);
        existFile.delete();
    }

    private String createFilename() {
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
