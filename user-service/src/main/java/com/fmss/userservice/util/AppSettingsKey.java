package com.fmss.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppSettingsKey {
    public static final String APPLICATION_MAIL_SOURCE_URL = "advicemy.base-url";
    public static final String CREATE_PASSWORD_URL_FORMAT = "%s/create-password?token=%s&uid=%s";
    public static final String RESET_PASSWORD_URL_FORMAT = "%s/reset-password?token=%s&uid=%s";
    public static final String APPLICATION_SOURCE_URL_TEMPLATE_KEY = "APPLICATION_SOURCE_URL";


    public static final String SEND_EMAIL_FIRST_NAME = "firstName";
    public static final String SEND_EMAIL_LAST_NAME = "lastName";
    public static final String SEND_EMAIL_USERNAME = "username";
    public static final String SEND_EMAIL_TOKEN_LINK = "tokenLink";
}
