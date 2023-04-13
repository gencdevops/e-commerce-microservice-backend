package com.fmss.userservice.mail;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String from;
    private Collection<String> to;
    private String subject;
    private String template;
    private Map<String, Object> parameters;
    private Collection<String> replyTo;
    private Collection<MailResource> attachments;

    public static final class EmailBuilder {
        private String from;
        private Collection<String> to;
        private String subject;
        private String template;
        private Map<String, Object> parameters;
        private Collection<String> replyTo;
        private Collection<MailResource> attachments;

        private EmailBuilder() {
        }

        public static EmailBuilder anEmail() {
            return new EmailBuilder();
        }

        public EmailBuilder withFrom(String from) {
            this.from = from;
            return this;
        }

        public EmailBuilder withTo(String to) {
            this.to = Collections.singleton(to);
            return this;
        }

        public EmailBuilder withTo(Collection<String> to) {
            this.to = to;
            return this;
        }

        public EmailBuilder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailBuilder withTemplate(String template) {
            this.template = template;
            return this;
        }

        public EmailBuilder withParameter(String key, Object value) {
            if (this.parameters == null) {
                this.parameters = new HashMap<>();
            }
            this.parameters.put(key, value);
            return this;
        }

        public EmailBuilder withParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public EmailBuilder withReplyTo(Collection<String> replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public EmailBuilder withAttachments(Collection<MailResource> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Email build() {
            Email email = new Email();
            email.setFrom(from);
            email.setTo(to);
            email.setSubject(subject);
            email.setTemplate(template);
            email.setParameters(parameters);
            email.setReplyTo(replyTo);
            email.setAttachments(attachments);
            return email;
        }
    }
}
