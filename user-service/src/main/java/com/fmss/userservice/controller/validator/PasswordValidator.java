package com.fmss.userservice.controller.validator;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Component
public class PasswordValidator {

    public void validate(UserDetails userDetail, String newPassword) {
        checkIfPasswordContainsUserInfo(userDetail, newPassword);
    }

    private void checkIfPasswordContainsUserInfo(UserDetails userDetail, String password) {
        if (containsIgnoreCaseViceVersa(password, userDetail.getUsername())
                || containsIgnoreCaseViceVersa(password, userDetail.getUsername())) {
            throw new RuntimeException("error.passwordContainsUserInfo");
        }
    }

    private boolean containsIgnoreCaseViceVersa(String firstValue, String secondValue) {
        return containsIgnoreCase(firstValue, secondValue) || containsIgnoreCase(secondValue, firstValue);
    }
}
