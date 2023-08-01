package com.readingbooks.web.service.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class AwsS3ImageUploadUtil implements ImageUploadUtil{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        /* --- 이름 생성 --- */
        String filename = createFilename();
        String fileExtension = extractExtension(file.getOriginalFilename());
        String saveFilename = getSaveFilename(filename, fileExtension);

        /* --- 메타 정보 생성 --- */
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(bucket, saveFilename, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.debug("uploading error = {}", e.getStackTrace());
        }

        return saveFilename;
    }

    private String getSaveFilename(String filename, String fileExtension) {
        return filename + "." + fileExtension;
    }

    private String createFilename() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String update(MultipartFile file, String existingImageName) {
        delete(existingImageName);
        return upload(file);
    }

    @Override
    public void delete(String savedImageName) {
        amazonS3Client.deleteObject(bucket, savedImageName);
    }

    private String extractExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }
}
