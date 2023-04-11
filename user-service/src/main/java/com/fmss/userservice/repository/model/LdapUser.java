package com.fmss.userservice.repository.model;

import com.fmss.userservice.model.enums.UserStatus;
import lombok.Data;

@Data
public class LdapUser {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private UserStatus status;
}
