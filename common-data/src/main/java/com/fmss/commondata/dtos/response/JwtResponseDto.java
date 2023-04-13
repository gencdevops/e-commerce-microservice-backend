package com.fmss.commondata.dtos.response;

import java.io.Serial;
import java.io.Serializable;

public record JwtResponseDto(String jwtToken) implements Serializable {
    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
}