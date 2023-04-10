package com.fmss.userservice.model.dto.request;

import com.fmss.userservice.model.entity.User;
import com.fmss.userservice.model.enums.UserStatus;
import com.fmss.userservice.validation.annotation.CustomEmail;

import javax.validation.constraints.Size;

public record UserRegisterRequestDto(

        String firstName,
        String lastName,
        String birthDate,
        @CustomEmail
        String email,
        @Size(max = 128)
        String password
) {
        public User toUser() {
                var user = new User();
                user.setUserStatus(UserStatus.ACTIVE);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPassword(password);
                user.setBirthDate(birthDate);
                user.setUserName(email);
                return user;
        }
}
