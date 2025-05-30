package com.myralla.mailinator.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${cors1.url}")
    private String cors1Url;

    @Value("${cors2.url}")
    private String cors2Url;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Use /** to apply to all paths
                        .allowedOrigins("*") // Accept requests from any origin
                        .allowedMethods("*")        // Allow all HTTP methods
                        .allowedHeaders("*")        // Allow all headers
                        .allowCredentials(false);    // Allow credentials (cookies, auth headers, etc.)
            }
        };
    }
}