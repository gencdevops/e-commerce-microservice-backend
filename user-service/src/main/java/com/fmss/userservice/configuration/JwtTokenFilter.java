package com.fmss.userservice.configuration;

import com.fmss.userservice.util.Validations;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String JSESSIONID = "JSESSIONID";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsConfig userDetailsConfig;

    public JwtTokenFilter(
            JwtTokenUtil jwtTokenUtil,
            UserDetailsConfig userDetailsConfig) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsConfig = userDetailsConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = parseJwt(request);
        String username = null;

        if (nonNull(token)) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException ex) {
                throw new BadCredentialsException(Validations.ERR_WRONG_USERNAME_OR_PASSWORD);
            } catch (ExpiredJwtException e) {
                //TODO Token Expire exception
                throw new BadCredentialsException(Validations.ERR_WRONG_USERNAME_OR_PASSWORD);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsConfig.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(token, userDetails.getUsername()))) {
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
        String headerAuth = request.getHeader(AUTHORIZATION);

        if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
