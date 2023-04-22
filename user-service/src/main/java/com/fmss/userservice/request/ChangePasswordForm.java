package com.fmss.userservice.request;

public record ChangePasswordForm(
        String currentPassword,
        String newPassword
) {
}
