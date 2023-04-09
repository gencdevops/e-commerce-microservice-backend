package com.fmss.userservice.configuration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class EmailConfiguration {
    public static final String MAIL_SEND_EXECUTOR = "mailSendThreadPoolTaskExecutor";

    @Bean(name = MAIL_SEND_EXECUTOR, destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor mailSendExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setCorePoolSize(0);
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setQueueCapacity(1000);
        return threadPoolTaskExecutor;
    }

}

