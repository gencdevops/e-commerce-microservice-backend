package com.fmss.orderservice.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutBoxRetryTask {

    private final OutBoxRetryService outBoxRetryService;

    public OutBoxRetryTask(OutBoxRetryService outBoxRetryService) {
        this.outBoxRetryService = outBoxRetryService;
    }


    @Scheduled(fixedDelay = 10000)
    public void retry() {
        outBoxRetryService.retry();
    }
}

