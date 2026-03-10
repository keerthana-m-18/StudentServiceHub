package com.studenthub.studentservicehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- ADD THIS IMPORT

@SpringBootApplication
@EnableScheduling // <-- ADD THIS ANNOTATION
public class StudentServiceHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentServiceHubApplication.class, args);
    }
}
