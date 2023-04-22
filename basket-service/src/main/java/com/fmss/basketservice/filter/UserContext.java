package com.fmss.basketservice.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

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

