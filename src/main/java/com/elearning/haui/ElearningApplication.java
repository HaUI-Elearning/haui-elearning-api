package com.elearning.haui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ElearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElearningApplication.class, args);
    }

}
