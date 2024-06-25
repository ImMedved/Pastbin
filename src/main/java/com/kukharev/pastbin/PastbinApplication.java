package com.kukharev.pastbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kukharev.pastbin"})
public class PastbinApplication {
    public static void main(String[] args) {
        SpringApplication.run(PastbinApplication.class, args);
    }
}