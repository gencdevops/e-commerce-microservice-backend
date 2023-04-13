package com.fmss.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validations {

    public static final String CUSTOMER_ID_INVALID = "error.customerIdInvalid";

    public static final String ERR_USER_NOTFOUND = "error.userNotFound";
    public static final String ERR_WRONG_USERNAME_OR_PASSWORD = "error.wrongUsernameOrPassword";
    public static final String ERR_USER_ACCOUNT_DISABLED = "error.userAccountDisabled";

    public static final String ERR_INVALID_FORGOT_PASSWORD_TOKEN = "error.forgotPasswordTokenInvalid";



}
