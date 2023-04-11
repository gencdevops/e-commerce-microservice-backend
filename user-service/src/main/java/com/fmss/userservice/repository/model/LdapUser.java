package com.fmss.userservice.repository.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(
        base = "ou=Users,o=64346c9136393df65a68908f,dc=jumpcloud,dc=com",
        objectClasses = {
                "person",
                "inetOrgPerson",
                "jumpcloudUser",
                "organizationalPerson",
                "top",
                "posixAccount",
                "shadowAccount"
        })
public class LdapUser {
    @Id
    private Name id;

    private String userPrincipalName;
    @Attribute(name = "cn")
    private String givenName;
    @Attribute(name = "sn")
    private String sn;
    @Attribute(name = "mail")
    private String mail;
    @Attribute(name = "userPassword")
    private String userPassword;
}
