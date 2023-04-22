package com.fmss.userservice.exeption;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }

    public static void throwIfHasErrors(Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }
}
