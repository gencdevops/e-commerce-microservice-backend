package com.fmss.userservice.constants;

public final class UserConstants {
    private UserConstants() {}

    public static final String API_PREFIX = "/api";
    public static final String API_VERSION_V1 = "/v1";

    public static final String API_USER = "/users";
    public static final String API_USER_REGISTER = "/register";
    public static final String API_USER_VALIDATE = "/validate-token/{token}";
    public static final String API_USER_FORGET = "/forget-password/{email}";

    public static final String OTP_REDIS_KEY = "-otp";

    public static final String JSESSIONID = "JSESSIONID";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String JWT_TOKEN_EXPIRED = "JwtToken expired";
    public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
    public static final String CUSTOMER_ID_INVALID = "CustomerId invalid.";
    public static final String CURRENT_AND_BEFORE_PASSWORD_NOT_MATCH = "Current and before password not match";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id";
    public static final String USER_ACCOUNT_DISABLED = "User account disabled";
    public static final String USER_NOT_FOUND = "User not found";
}
