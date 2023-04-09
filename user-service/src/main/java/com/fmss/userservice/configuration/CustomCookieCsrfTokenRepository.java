package com.fmss.userservice.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.DeferredCsrfToken;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.UUID;


@RequiredArgsConstructor
public final class CustomCookieCsrfTokenRepository implements CsrfTokenRepository {
    private final CsrfCookieConfigurationProperties configuration;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(
                configuration.getHeaderName(),
                configuration.getParameterName(),
                createNewToken()
        );
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request,
                          HttpServletResponse response) {
        final String tokenValue = token == null ? "" : token.getToken();

        final Cookie cookie = new Cookie(configuration.getCookieName(), tokenValue);
        cookie.setSecure(configuration.isCookieSecure());
        final String cookiePath = configuration.getCookiePath();
        if (cookiePath != null && !cookiePath.isEmpty()) {
            cookie.setPath(cookiePath);
        } else {
            cookie.setPath(this.getRequestContext(request));
        }

        if (token == null) {
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(configuration.getCookieMaxAge());
        }

        cookie.setHttpOnly(configuration.isCookieHttpOnly());
        final String cookieDomain = configuration.getCookieDomain();
        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }

        response.addCookie(cookie);
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, configuration.getCookieName());
        if (cookie == null) {
            return null;
        }
        String token = cookie.getValue();
        if (!StringUtils.hasLength(token)) {
            return null;
        }
        return new DefaultCsrfToken(configuration.getHeaderName(), configuration.getParameterName(), token);
    }

    @Override
    public DeferredCsrfToken loadDeferredToken(HttpServletRequest request, HttpServletResponse response) {
        return CsrfTokenRepository.super.loadDeferredToken(request, response);
    }

    private String getRequestContext(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }

    private String createNewToken() {
        return UUID.randomUUID().toString();
    }
}
