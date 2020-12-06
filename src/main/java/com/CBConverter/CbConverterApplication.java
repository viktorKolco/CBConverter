package com.CBConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com"})
public class CbConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbConverterApplication.class, args);
    }
}
