package com.fmss.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.apigateway.dto.JwtTokenDto;
import com.fmss.commondata.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidateInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = null;

        try {
            token = request.getHeader("token");
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));
            var userDetails = new ObjectMapper().readValue(payload, JwtTokenDto.class);
            log.info("interceptor write" + token);
            return jwtUtil.validateToken(token, userDetails.getUserName());
        } catch (Exception ex) {
            //
        }
        return true;
    }

}
