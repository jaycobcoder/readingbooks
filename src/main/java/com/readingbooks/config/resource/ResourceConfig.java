package com.readingbooks.config.resource;

import com.readingbooks.config.PathConst;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    private String imageResourcePath = "file:///"+ PathConst.IMAGE_SAVE_PATH;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PathConst.IMAGE_PATH)
                .addResourceLocations(imageResourcePath);
    }
}
