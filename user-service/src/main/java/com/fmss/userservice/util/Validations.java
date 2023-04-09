package com.fmss.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validations {
    public static final String REQUIRED = "Bu alan bos birakilamaz.";
    public static final String EMAIL_CHECK = "Email adresi gecerli degil.Lutfen gecerli bir email adresi giriniz.";

    public static final String ADVISOR_ID_INVALID = "error.advisorIdInvalid";
    public static final String COUPON_ID_INVALID = "error.couponIdInvalid";
    public static final String BASKET_ID_INVALID = "error.basketIdInvalid";
    public static final String ADVISOR_NOTE_ID_INVALID = "error.advisorNoteIdInvalid";
    public static final String ADVISOR_USERNAME_INVALID = "error.advisorUsernameInvalid";
    public static final String CUSTOMER_ID_INVALID = "error.customerIdInvalid";
    public static final String CUSTOMER_OLD_PASSWORD_INVALID = "error.customerOldPasswordInvalid";
    public static final String CUSTOMER_PASSWORD_NOTEQUAL = "error.customerPasswordNotEqualConfirm";
    public static final String FILE_UPLOAD_ERROR = "error.fileUploadError";
    public static final String CATEGORY_ID_INVALID = "error.categoryIdInvalid";
    public static final String POST_ID_INVALID = "error.postIdInvalid";
    public static final String APP_SETTINGS_INVALID = "error.appSettingsInvalid";
    public static final String BLOG_ID_INVALID = "error.blogIdInvalid";

    public static final String SECURITY_LOGIN_REQUEST = "_security_login_request_";

    public static final String ERR_NOT_AUTHORIZED = "error.notAuthorized";
    public static final String ERR_USER_NOTFOUND = "error.userNotFound";
    public static final String ERR_WRONG_USERNAME_OR_PASSWORD = "error.wrongUsernameOrPassword";
    public static final String ERR_USER_ACCOUNT_DISABLED = "error.userAccountDisabled";
    public static final String ERR_USER_ACCOUNT_USED_EMAIL = "error.userAccountUsedEmail";
    public static final String ERR_USER_ACCOUNT_EXPIRED = "error.userAccountExpired";
    public static final String ERR_NO_PACKAGE_FOUND = "error.packageIsNotExist";
    public static final String BLOG_CATEGORY_ID_INVALID = "error.blogCategoryIdInvalid";
    public static final String ERR_INVALID_FORGOT_PASSWORD_TOKEN = "error.forgotPasswordTokenInvalid";
    public static final String APPOINTMENT_ID_INVALID = "error.appointmentIdInvalid";
    public static final String COUPON_CODE_INVALID = "error.couponCodeInvalid";

    public static final String OPTIONS_NOT_FOUND_WITH_GIVEN_ID = "options.not.found.with.given.user";


}
