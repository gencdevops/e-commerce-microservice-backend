package com.fmss.userservice.request;

public record ResetPasswordForm(
        String uid,
        String password,
        String token
) {
}
