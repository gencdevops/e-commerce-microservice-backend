package com.fmss.userservice.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Muhammed ALAGOZ
 */

@Getter
@Setter
@ToString
public class LoginRequest {
    private String username;
    @ToString.Exclude
    private String password;
}
