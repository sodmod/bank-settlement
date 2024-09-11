package org.badmus.settlement.cloudinary.config;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.unit.DataSize;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Badmus Sodiq Olusola \n
 * @description This class defines all the application configuration
 */
@Configuration
public class CloudinaryConfig {

    public static final String maxSize = "10000MB";

    private static final String cloud_name = "dk9uxcpvr";
    private static final String api_key = "292934753593799";
    private static final String api_secret = "9TGvliMjB1jrsqfcwXLdJV3N_e8";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloud_name);
        config.put("api_key", api_key);
        config.put("api_secret", api_secret);
        return new Cloudinary(config);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(maxSize));
        factory.setMaxRequestSize(DataSize.parse(maxSize));
        return factory.createMultipartConfig();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return builder;
    }

    @Bean(name = "uploadToCloudinaryExecutorThread")
    public Executor uploadToCloudinaryExecutorThread() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setThreadNamePrefix("uploadToCloudinaryExecutorThread-");
        executor.initialize();
        return executor;
    }

}
