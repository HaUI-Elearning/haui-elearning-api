package com.elearning.haui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Cấu hình CORS cho các API
                        .allowedOrigins("http://127.0.0.1:5173", "http://localhost:5173") // Cho phép các origin cụ thể
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method được phép
                        .allowedHeaders("*") // Các header được phép
                        .allowCredentials(true); // Nếu cần gửi cookie
            }
        };
    }
}
