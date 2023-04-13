package com.fmss.userservice.mail;

import com.fmss.userservice.model.entity.LdapUser;

import java.util.Map;
import java.util.Set;

public interface MailingService {
    void sendForgotPasswordEmail(String email, String userName, String lastName, String link);

    void sendEmail(String emailTemplate, Map<String, Object> params, String subject, Set<String> to);

    void sendUserAccountCreatedEmail(LdapUser user, String tokenLink);

    void sendOtpEmail(String mail, String givenName, String sn, String otp);
}
