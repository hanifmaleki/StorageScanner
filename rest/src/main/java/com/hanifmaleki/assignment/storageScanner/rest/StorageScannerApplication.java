package com.hanifmaleki.assignment.storageScanner.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Import(CoreConfigurationReference.class)
@EnableScheduling
public class StorageScannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageScannerApplication.class);
    }
}
