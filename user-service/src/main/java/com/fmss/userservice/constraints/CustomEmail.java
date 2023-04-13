package com.fmss.userservice.constraints;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Constraint(validatedBy = {
        CustomEmail.CustomEmailStringValidator.class
})
public @interface CustomEmail {
    String message() default "Email düzgün formatta değil.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomEmailStringValidator implements ConstraintValidator<CustomEmail, String> {

        CustomEmail emailAnnotation;

        @Override
        public void initialize(CustomEmail constraintAnnotation) {
            this.emailAnnotation = constraintAnnotation;
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
            var pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(value);
            return matcher.find();
        }
    }
}
