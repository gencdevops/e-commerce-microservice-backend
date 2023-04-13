package com.fmss.userservice.controller.validator;

import com.fmss.userservice.Validations;
import com.fmss.userservice.exeption.ValidationException;
import com.fmss.userservice.request.ResetPasswordForm;
import com.fmss.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
@RequiredArgsConstructor
public class ResetPasswordValidator {
    private final PasswordValidator passwordValidator;
    private final UserService userService;

    public void validate(ResetPasswordForm form) {
        final BindingResult errors = new BeanPropertyBindingResult(form, "ResetPasswordForm");
        rejectIfEmptyOrWhitespace(errors, "uid", Validations.REQUIRED);
        rejectIfEmptyOrWhitespace(errors, "token", Validations.REQUIRED);
        rejectIfEmptyOrWhitespace(errors, "password", Validations.REQUIRED);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        final UserDetails userDetail = userService.getUserByForgotPasswordToken(form.uid(), form.token());
        passwordValidator.validate(userDetail, form.password());
    }

}
