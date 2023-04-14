package com.fmss.commondata.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.dtos.response.JwtTokenResponseDto;
import com.fmss.commondata.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

import static org.apache.commons.lang.BooleanUtils.isFalse;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidateConfiguration implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Value("${jwt.enabled:false}")
    private boolean isJwtEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isFalse(isJwtEnabled)) {
            return true;
        }

        try {
            final var token = parseJwt(request);
            if (!StringUtils.hasText(token)) {
                return false;
            }
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
