package com.fmss.apigateway.dto;

import lombok.Data;

@Data
public class JwtTokenDto {
    private String userName;
    private String userId;
    private String email;
    private String sub;
    private Integer iat;
    private Integer exp;
}
