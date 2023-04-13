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
}
