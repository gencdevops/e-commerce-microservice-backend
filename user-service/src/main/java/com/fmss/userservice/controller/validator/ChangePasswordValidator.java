package com.fmss.userservice.controller.validator;

import com.fmss.userservice.Validations;
import com.fmss.userservice.exeption.ValidationException;
import com.fmss.userservice.request.ChangePasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;

import static org.apache.commons.lang3.StringUtils.length;

@Component
@RequiredArgsConstructor
public class ChangePasswordValidator {

    public void validate(ChangePasswordForm form) {
        final BindingResult errors = new BeanPropertyBindingResult(form, "ChangePasswordForm");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", Validations.REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", Validations.REQUIRED);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        if (length(form.newPassword()) < 8) {
            errors.reject("error.passwordLength");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }
}
