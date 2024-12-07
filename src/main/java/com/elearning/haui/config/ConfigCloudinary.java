package com.elearning.haui.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap();
        config.put("cloud_name", "dzotsteiv");
        config.put("api_key", "952641981189691");
        config.put("api_secret", "WNe7WBVvPwh3hYbgeyJs19_30RE");
        return new Cloudinary(config);
    }

}