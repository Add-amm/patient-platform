package com.chronic.vitals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.chronic.vitals.model")
@EnableJpaRepositories("com.chronic.vitals.service")
public class VitalsIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalsIngestionApplication.class, args);
    }
}