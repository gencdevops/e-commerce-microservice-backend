package com.fmss.userservice.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
public record JwtResponseDto(String jwtToken) implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
}