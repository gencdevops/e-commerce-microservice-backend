package com.fmss.userservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Muhammed ALAGOZ
 */

@Log4j2
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        LOG.info("handle::accessDeniedException={}", accessDeniedException.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());

        if (accessDeniedException instanceof MissingCsrfTokenException) {
            objectMapper.writeValue(response.getOutputStream(), Map.of("code", "error.missingCsrf"));
            return;
        }

        if (accessDeniedException instanceof InvalidCsrfTokenException) {
            objectMapper.writeValue(response.getOutputStream(), Map.of("code", "error.invalidCsrf"));
            return;
        }

        objectMapper.writeValue(response.getOutputStream(), Map.of("code", "error.accessDenied"));
    }
}