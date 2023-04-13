package com.fmss.commondata.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;




public record JwtResponseDto(String jwtToken) implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
}