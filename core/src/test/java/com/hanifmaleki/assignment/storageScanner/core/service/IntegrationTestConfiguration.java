package com.hanifmaleki.assignment.storageScanner.core.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.hanifmaleki.assignment.storageScanner.core")
@EntityScan("com.hanifmaleki.assignment.storageScanner.core.model")
@EnableJpaRepositories("com.hanifmaleki.assignment.storageScanner.core.repository")
@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = "com.hanifmaleki.assignment.storageScanner.core")
public class IntegrationTestConfiguration {

}
