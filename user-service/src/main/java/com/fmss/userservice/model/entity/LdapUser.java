package com.fmss.userservice.model.entity;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(base = "ou=Users,dc=fmss,dc=com", objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
public class LdapUser {

    private @Id Name id;
    private @DnAttribute(value = "uid") String uid;
    private @Attribute(name = "cn") String userPrincipalName;
    private @Attribute(name = "givenName") String givenName;
    private @Attribute(name = "sn") String sn;
    private @Attribute(name = "mail") String mail;
    private String userPassword;

    public String generateCreatePasswordToken() {
        return DigestUtils.md5Hex("CreatePassword|" + getMail() + "|" + getGivenName() + "|" + getUserPassword());
    }

    public String generateResetPasswordToken() {
        return DigestUtils.md5Hex("Reset|" + getMail() + "|" + getGivenName() + "|" + getUserPassword());
    }

    public String generateOtpToken(String otp) {
        return DigestUtils.md5Hex("Otp|" + otp);
    }
}
