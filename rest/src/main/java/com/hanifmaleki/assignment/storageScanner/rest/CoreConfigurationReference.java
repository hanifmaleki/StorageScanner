package com.hanifmaleki.assignment.storageScanner.rest;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.hanifmaleki.assignment.storageScanner.core")
@EnableJpaRepositories("com.hanifmaleki.assignment.storageScanner.core.repository")
@EntityScan("com.hanifmaleki.assignment.storageScanner.core.model")
@EnableScheduling
public class CoreConfigurationReference {
}
