package com.CBConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CbConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbConverterApplication.class, args);
    }
}
