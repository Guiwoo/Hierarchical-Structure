package com.example.dowhateveriwant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DoWhatEverIWantApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoWhatEverIWantApplication.class, args);
    }

}
