package com.fmss.userservice.repository.model;

import lombok.Data;

@Data
public class LdapUser {
    private String userPrincipalName;
    private String givenName;
    private String sn;
    private String mail;
    private String userPassword;
}
