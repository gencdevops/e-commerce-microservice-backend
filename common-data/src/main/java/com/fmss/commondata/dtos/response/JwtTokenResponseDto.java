package com.fmss.commondata.dtos.response;

public record JwtTokenResponseDto(String userName, String userId, String email, String sub, Integer iat, Integer exp) {
}
