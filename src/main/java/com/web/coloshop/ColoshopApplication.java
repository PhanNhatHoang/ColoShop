package com.web.coloshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ColoshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColoshopApplication.class, args);
    }

}
