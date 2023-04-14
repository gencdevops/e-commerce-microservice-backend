package com.fmss.orderservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.dtos.response.JwtTokenResponseDto;
import com.fmss.commondata.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidateConfiguration implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final var token = parseJwt(request);

        if(Strings.isEmpty(token)) {
            return false;
        }

        try {

            final var chunks = token.split("\\.");
            final var decoder = Base64.getUrlDecoder();

            final var header = new String(decoder.decode(chunks[0]));
            final var payload = new String(decoder.decode(chunks[1]));
            final var userDetails = new ObjectMapper().readValue(payload, JwtTokenResponseDto.class);
            final var userName = userDetails.email();
            boolean isValidToken = jwtUtil.validateToken(token, userName);
            
            log.info("TokenValidateInterceptor::token validating:{}::userName:{}", isValidToken, userName);
            return isValidToken;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


    }

    private String parseJwt(HttpServletRequest request) {
        final var headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }
        return "";
    }

}
