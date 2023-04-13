package com.fmss.userservice.request;

import com.fmss.userservice.model.LdapUser;

import javax.validation.constraints.Size;

public record UserRegisterRequestDto(
        String firstName,
        String lastName,
        String userName,
        String email,
        @Size(max = 128)
        String password

) {
        public LdapUser toUser() {
                var user = new LdapUser();
                user.setGivenName(firstName);
                user.setSn(lastName);
                user.setMail(email);
                user.setUserPassword(password);
                user.setUid(userName);
                return user;
        }
}
