package com.cliptripbe.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("placeSearchExecutor")
    public Executor placeSearchExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(20);
        exec.setMaxPoolSize(30);
        exec.setQueueCapacity(100);
        exec.setThreadNamePrefix("place-exec-");
        exec.setAllowCoreThreadTimeOut(true);
        exec.initialize();
        return exec;
    }

    @Bean("videoExtractExecutor")
    public Executor videoExtractExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(10);
        exec.setMaxPoolSize(20);
        exec.setQueueCapacity(25);
        exec.setThreadNamePrefix("video-exec-");
        exec.setAllowCoreThreadTimeOut(true);
        exec.initialize();
        return exec;
    }
}
