package com.fmss.userservice.configuration.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void send(Email mail) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        if (!CollectionUtils.isEmpty(mail.getAttachments())) {
            mail.getAttachments()
                    .forEach(mailResource ->
                            {
                                try {
                                    helper.addAttachment(
                                            mailResource.getAttachmentFilename(),
                                            mailResource.getFile()
                                    );
                                } catch (MessagingException | jakarta.mail.MessagingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }
        Context context = new Context();
        context.setVariables(mail.getParameters());

        String html = templateEngine.process(mail.getTemplate(), context);
        helper.setTo(mail.getTo().toArray(new String[0]));
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);
    }
}
