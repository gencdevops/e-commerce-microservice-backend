package com.fmss.userservice.mail;

import com.fmss.userservice.model.LdapUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static com.fmss.userservice.util.AppSettingsKey.APPLICATION_SOURCE_URL_TEMPLATE_KEY;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {
    private static final String TEMPLATE_FORGOT_PASSWORD = "forgot-password";
    private static final String TEMPLATE_NEW_USER_CREATED = "newUserCreated";
    private static final String TEMPLATE_OTP = "verify-otp";

    private static final String SEND_EMAIL_FIRST_NAME = "firstName";
    private static final String SEND_EMAIL_LAST_NAME = "lastName";
    private static final String SEND_EMAIL_USERNAME = "username";
    private static final String SEND_EMAIL_TOKEN_LINK = "tokenLink";

    private final EmailSenderService emailSender;


    //@Async(value = MAIL_SEND_EXECUTOR)
    public void sendForgotPasswordEmail(String to, String firstName, String lastName, String link) {
        final Email email = Email.EmailBuilder.anEmail()
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
    @Async(value = EmailConfiguration.MAIL_SEND_EXECUTOR)
    public void sendEmail(String emailTemplate, Map<String, Object> params, String subject, Set<String> to) {
        final Email email = Email.EmailBuilder.anEmail()
                .withFrom(getFromMailAddress())
                .withTo(to)
                .withSubject(subject)
                .withTemplate(emailTemplate)
                .withParameters(params)
                .build();
        sendEmail(email);
    }

    @Override
    public void sendUserAccountCreatedEmail(LdapUser user, String tokenLink) {
        final Email email = Email.EmailBuilder.anEmail()
                .withFrom(getFromMailAddress())
                .withTo(user.getMail())
                .withSubject(getCreatePasswordEmailSubject())
                .withTemplate(TEMPLATE_NEW_USER_CREATED)
                .withParameter(SEND_EMAIL_FIRST_NAME, user.getGivenName())
                .withParameter(SEND_EMAIL_LAST_NAME, user.getSn())
                .withParameter(SEND_EMAIL_USERNAME, user.getUid())
                .withParameter(SEND_EMAIL_TOKEN_LINK, tokenLink)
                .withParameter(APPLICATION_SOURCE_URL_TEMPLATE_KEY, getApplicationBaseUrl())
                .build();
        sendEmail(email);
    }

    @Override
    public void sendOtpEmail(String to, String firstName, String lastName, String otp) {
        final Email email = Email.EmailBuilder.anEmail()
                .withFrom(getFromMailAddress())
                .withTo(to)
                .withSubject("Fmss E-Com Otp bilgileriniz")
                .withTemplate(TEMPLATE_OTP)
                .withParameter("firstName", firstName)
                .withParameter("lastName", lastName)
                .withParameter("otp", otp)
                .withParameter(APPLICATION_SOURCE_URL_TEMPLATE_KEY, getApplicationBaseUrl())
                .build();

        sendEmail(email);
    }

    @SneakyThrows
    private void sendEmail(Email email) {
        emailSender.send(email);
    }

    private String getForgotPasswordEmailSubject() {
        return "Sifrenizi mi unuttunuz";
    }

    private String getCreatePasswordEmailSubject() {
        return "yeni sifre olusturuldu";
    }

    private String getApplicationBaseUrl() {
        return "http://localhost:3000";
    }

    private String getFromMailAddress() {
        return  "ecomfmss@gmail.com";
    }
}
