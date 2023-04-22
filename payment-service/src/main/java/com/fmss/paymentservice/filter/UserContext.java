package com.fmss.paymentservice.filter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor

public class UserContext {
    private String userName;
    private String userId;

    public void clear() {
        this.userName = null;
        this.userId = null;
    }
}

