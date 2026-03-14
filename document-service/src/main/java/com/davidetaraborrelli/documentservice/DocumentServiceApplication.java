package com.davidetaraborrelli.documentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.davidetaraborrelli.documentservice", "com.davidetaraborrelli.common"})
@EnableJpaRepositories(basePackages = "com.davidetaraborrelli.documentservice.repository.jpa")
@EnableMongoRepositories(basePackages = "com.davidetaraborrelli.documentservice.repository.mongo")
public class DocumentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
}
