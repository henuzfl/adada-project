package com.dataclouds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Hello world!
 */

@SpringBootApplication
@EnableConfigurationProperties
public class DatasetApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatasetApplication.class, args);
    }
}
