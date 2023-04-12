package com.fmss.userservice.repository.model;

import lombok.Data;

import javax.naming.Name;

@Data
public class LdapUser {
    private Name id;
    private String uid;
    private String userPrincipalName;
    private String givenName;
    private String sn;
    private String mail;
    private String userPassword;
}
