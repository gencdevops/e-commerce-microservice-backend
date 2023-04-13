package com.fmss.userservice.configuration.mail;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Email {
    private String from;
    private Collection<String> to;
    private String subject;
    private String template;
    private Map<String, Object> parameters;
    private Collection<String> replyTo;
    private Collection<MailResource> attachments;

}