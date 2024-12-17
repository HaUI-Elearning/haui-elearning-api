package com.elearning.haui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Tạo một đối tượng cấu hình CORS
        CorsConfiguration configuration = new CorsConfiguration();

        // Định nghĩa các nguồn cho phép (origins) - nơi mà yêu cầu có thể xuất phát từ
        // đó
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173", "http://localhost:5173"));

        // Định nghĩa các phương thức HTTP cho phép (GET, POST, PUT, DELETE, OPTIONS)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Định nghĩa các header mà client có thể gửi (Authorization, Content-Type,
        // Accept, ...)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // Cho phép gửi thông tin chứng thực (cookies hoặc Authorization headers) cùng
        // với yêu cầu CORS
        configuration.setAllowCredentials(true);

        // Cấu hình thời gian bộ nhớ cache cho yêu cầu pre-flight
        configuration.setMaxAge(3600L); // Cài đặt thời gian hết hạn bộ đệm của yêu cầu pre-flight (3600 giây = 1 giờ)

        // Tạo một UrlBasedCorsConfigurationSource và đăng ký cấu hình CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Áp dụng cấu hình CORS cho mọi yêu cầu URL
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
