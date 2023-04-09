package com.fmss.userservice.configuration;

import lombok.Data;

/**
 * @author Muhammed ALAGOZ
 */

@Data
public class CsrfCookieConfigurationProperties {
    private static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    private static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;
    private String headerName = DEFAULT_CSRF_HEADER_NAME;
    private String cookieName = DEFAULT_CSRF_COOKIE_NAME;
    private boolean cookieHttpOnly = false;
    private boolean cookieSecure = false;
    private boolean csrfCookieEnable = false;
    private int cookieMaxAge = -1;
    private String cookiePath;
    private String cookieDomain;
}
