package com.fmss.apigateway.constants;

public class GatewayConstants {

    private GatewayConstants(){

    }
    // Gateway versionlar
    public static final String CONTROLLER_VERSION_VARIABLE = "/{version}";
    public static final String CONTROLLER_V1_PREFIX = "/v1";


    //Gateway Prefix'ler


    //Gateway Api'ler


   //User Service Apis
   public static final String API_PREFIX = "/api";
    public static final String API_VERSION_V1 = "/v1";

    public static final String API_USER = "/users";
    public static final String API_USER_REGISTER = "/register";
    public static final String API_USER_VALIDATE = "/validate-token/{token}";
    public static final String API_USER_FORGET = "/forget-password/{email}";

}
