package com.fmss.userservice.model;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

@Data
public class LdapUser {

    private String uid;
    private String userPrincipalName;
    private String givenName;
    private String sn;
    private String mail;
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
