package com.example.watchex.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebMvc
@NoArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${endpoints.cors.allowed-methods}")
    private String[] methods;

    @Value("${localhost.path.client.user}")
    private String[] origins;

    @Value("${endpoints.cors.max-age}")
    private long maxAge;

    @Value("${endpoints.cors.allow-credentials}")
    private boolean allowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(methods)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
        registry.addResourceHandler("/product-photos/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
}
