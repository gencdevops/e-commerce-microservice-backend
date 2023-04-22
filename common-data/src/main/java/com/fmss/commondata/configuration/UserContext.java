package com.fmss.commondata.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserContext {
    private String userName;
    private String userId;

    public void clear() {
        this.userName = null;
        this.userId = null;
    }
}

