package com.fmss.userservice.configuration;

import com.advicemybackend.configuration.fault.ErrorsView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class returns 401 or 403 Http Status messages for Ajax requests instead of redirecting to login page.
 *
 * @author Muhammed ALAGOZ
 */
@Log4j2
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        LOG.debug("AuthenticationException: {}", authException.getMessage(), authException);
        final HttpStatus status = getHttpStatus(authException);
        response.setStatus(status.value());
        objectMapper.writeValue(response.getOutputStream(), new ErrorsView(authException.getMessage()));
    }

    private HttpStatus getHttpStatus(AuthenticationException authException) {
        final HttpStatus status;
        if (authException instanceof InsufficientAuthenticationException || authException instanceof AuthenticationCredentialsNotFoundException) {
            status = HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.FORBIDDEN;
        }
        return status;
    }
}
