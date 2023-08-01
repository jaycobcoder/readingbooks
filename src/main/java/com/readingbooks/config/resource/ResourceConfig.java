package com.readingbooks.config.resource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.readingbooks.config.PathConst;
import com.readingbooks.web.service.utils.AwsS3ImageUploadUtil;
import com.readingbooks.web.service.utils.ImageUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URL;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    /* --- aws s3 설정 --- */
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Bean
    public AmazonS3Client amazonS3Client(){
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }

    /* --- 이미지 유틸 설정 --- */
    @Bean
    public ImageUploadUtil imageUploadUtil(){
        return new AwsS3ImageUploadUtil(amazonS3Client());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        AmazonS3Client amazonS3Client = amazonS3Client();
        String url = amazonS3Client.getUrl(bucket, "").toString();
        registry.addResourceHandler(PathConst.IMAGE_PATH)
                .addResourceLocations(url);
    }
}
