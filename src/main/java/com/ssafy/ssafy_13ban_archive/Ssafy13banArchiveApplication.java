package com.ssafy.ssafy_13ban_archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Ssafy13banArchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ssafy13banArchiveApplication.class, args);
    }

}
