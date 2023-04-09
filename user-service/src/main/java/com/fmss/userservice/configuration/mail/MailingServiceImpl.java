package com.fmss.userservice.configuration.mail;

import com.fmss.userservice.configuration.ConfigurationHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static com.fmss.userservice.configuration.mail.Email.EmailBuilder.anEmail;
import static com.fmss.userservice.configuration.mail.EmailConfiguration.MAIL_SEND_EXECUTOR;
import static com.fmss.userservice.util.AppSettingsKey.APPLICATION_MAIL_SOURCE_URL;
import static com.fmss.userservice.util.AppSettingsKey.APPLICATION_SOURCE_URL_TEMPLATE_KEY;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {
    private static final String TEMPLATE_FORGOT_PASSWORD = "forgot-password";

    private final ConfigurationHolder configurationHolder;
    private final EmailSenderService emailSender;

    @Override
    @Async(value = MAIL_SEND_EXECUTOR)
    public void sendForgotPasswordEmail(String to, String firstName, String lastName, String link) {
        final Email email = anEmail()
                .withFrom(getFromMailAddress())
                .withTo(to)
                .withSubject(getForgotPasswordEmailSubject())
                .withTemplate(TEMPLATE_FORGOT_PASSWORD)
                .withParameter("firstName", firstName)
                .withParameter("lastName", lastName)
                .withParameter("url", link)
                .withParameter(APPLICATION_SOURCE_URL_TEMPLATE_KEY, getApplicationBaseUrl())
                .build();

        sendEmail(email);
    }

    @Override
    @Async(value = MAIL_SEND_EXECUTOR)
    public void sendEmail(String emailTemplate, Map<String, Object> params, String subject, Set<String> to) {
        final Email email = anEmail()
                .withFrom(getFromMailAddress())
                .withTo(to)
                .withSubject(subject)
                .withTemplate(emailTemplate)
                .withParameters(params)
                .build();
        sendEmail(email);
    }

    private void sendEmail(Email email) {
        emailSender.send(email);
    }

    private String getForgotPasswordEmailSubject() {
        return configurationHolder.getStringProperty("email.forgot-password-mail.subject");
    }

    private String getCreatePasswordEmailSubject() {
        return configurationHolder.getStringProperty("email.create-password-mail.subject");
    }

    private String getApplicationBaseUrl() {
        return configurationHolder.getStringProperty(APPLICATION_MAIL_SOURCE_URL);
    }

    private String getFromMailAddress() {
        final String fromMailAddress = configurationHolder.getStringProperty("email.smtp.from");
        return fromMailAddress;
    }
}