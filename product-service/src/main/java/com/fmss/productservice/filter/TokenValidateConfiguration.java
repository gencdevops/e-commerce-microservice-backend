package com.fmss.productservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.dtos.response.JwtTokenResponseDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Base64;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidateConfiguration implements Filter {

    private final JwtUtil jwtUtil = new JwtUtil();

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final var token = parseJwt((HttpServletRequest) request);

        if (Strings.isEmpty(token)) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden because of headers");
            return;
        }

        try {
            UserContext userContext = new UserContext();
            userContext.setUserId(jwtUtil.getUserDetailsFromToken(token).userId());
            final var chunks = token.split("\\.");
            final var decoder = Base64.getUrlDecoder();

            final var header = new String(decoder.decode(chunks[0]));
            final var payload = new String(decoder.decode(chunks[1]));
            final var userDetails = new ObjectMapper().readValue(payload, JwtTokenResponseDto.class);
            final var userName = userDetails.email();
            boolean isValidToken = jwtUtil.validateToken(token, userName);

            log.info("TokenValidateInterceptor::token validating:{}::userName:{}", isValidToken, userName);
        } catch (Exception e) {
            log.debug("logContextModel can not be init : {}", e.getMessage());
        }
    }

    private String parseJwt(HttpServletRequest request) {
        final var headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }
        return "";
    }



    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}