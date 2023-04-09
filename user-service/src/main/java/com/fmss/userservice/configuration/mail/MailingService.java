package com.fmss.userservice.configuration.mail;

import java.util.Map;
import java.util.Set;

public interface MailingService {
    void sendForgotPasswordEmail(String email, String userName, String link);

    void sendEmail(String emailTemplate, Map<String, Object> params, String subject, Set<String> to);
}
