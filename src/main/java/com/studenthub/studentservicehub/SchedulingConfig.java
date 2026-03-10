package com.studenthub.studentservicehub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling // Ensures Spring scans for @Scheduled methods
public class SchedulingConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // Set the number of threads for scheduling tasks
        scheduler.setThreadNamePrefix("scheduler-thread-");
        scheduler.initialize();
        return scheduler;
    }
}
