package com.fmss.userservice.jwt;


import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.configuration.UserDetailsConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.fmss.userservice.constants.UserConstants.*;
import static java.util.Objects.nonNull;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsConfiguration userDetailsConfig;

    public JwtTokenFilter(
            JwtUtil jwtUtil,
            UserDetailsConfiguration userDetailsConfig) {
        this.jwtUtil = jwtUtil;
        this.userDetailsConfig = userDetailsConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = parseJwt(request);
        String username = null;
        if (nonNull(token)) {
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException ex) {
                throw new BadCredentialsException(WRONG_USERNAME_OR_PASSWORD);
            } catch (ExpiredJwtException e) {
                throw new BadCredentialsException(JWT_TOKEN_EXPIRED);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsConfig.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails.getUsername()))) {
                setAuthentication(request, userDetails);
            }
        }
        chain.doFilter(request, response);
    }

    private static void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String parseJwt(HttpServletRequest request) {
        try {
            String headerAuth = request.getHeader(AUTHORIZATION);
            if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
                return headerAuth.substring(7);
            }
            logger.warn("Jwt parse error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
