package com.fmss.orderservice.service;


import com.fmss.orderservice.model.slack.SlackContextMessage;
import com.fmss.orderservice.model.slack.SlackDetailMessage;
import com.fmss.orderservice.model.slack.SlackMessage;
import com.fmss.orderservice.model.slack.SlackMessageBlock;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


@Component
@Slf4j
public class SlackReportingService {
    @Value("${slack.web.hook.base.url}")
    private String webHookBaseUrl;
    @Value("${slack.backend.errors.channel}")
    private String errorsChannel;

    private final RestTemplate restTemplate = getInstance();

    public void sendErrorMessage(String subject, Throwable e) {
        restTemplate.postForEntity(webHookBaseUrl + errorsChannel, getSlackMessage(subject, null, e), String.class);
    }

    public void sendErrorMessage(String subject, String exMessage) {
        sendErrorMessage(subject, new RuntimeException(exMessage));
    }

    private SlackMessageBlock getSlackMessage(String subject, String msg, Throwable e) {

        String stackTrace = e != null ? ExceptionUtils.getStackTrace(e).replace("\n", "<br>") : Strings.EMPTY;

        SlackMessage headerMessage = SlackMessage.builder()
                .text(SlackDetailMessage.builder()
                        .type("plain_text")
                        .text("😱" + subject + "😰")
                        .build())
                .type("header")
                .build();

        SlackContextMessage slackContextMessage = SlackContextMessage.builder()
                .elements(Collections.singletonList(SlackDetailMessage
                        .builder()
                        .type("mrkdwn")
                        .text("```" + (Objects.nonNull(msg) ? msg : StringUtils.EMPTY) + stackTrace + "```")
                        .build()))
                .type("context")
                .build();

        return SlackMessageBlock.builder()
                .blocks(Arrays.asList(headerMessage, slackContextMessage))
                .build();
    }

    public RestTemplate getInstance() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setConnectionRequestTimeout(1000);
        final RestTemplate template = new RestTemplate(requestFactory);
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error("ERROR TEXT: {} {}", response.getStatusCode(), response.getStatusText());
                log.error("response body: {} ", IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
                super.handleError(response);
            }
        });
        return template;
    }
}
