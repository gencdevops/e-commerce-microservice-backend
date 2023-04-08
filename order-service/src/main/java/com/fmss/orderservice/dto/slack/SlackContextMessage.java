package com.fmss.orderservice.dto.slack;

import java.util.List;

public class SlackContextMessage {
    private List<SlackDetailMessage> elements;
    private String type;

    public List<SlackDetailMessage> getElements() {
        return elements;
    }

    public void setElements(List<SlackDetailMessage> elements) {
        this.elements = elements;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static SlackContextMessageBuilder builder() {
        return new SlackContextMessageBuilder();
    }

    public static final class SlackContextMessageBuilder {
        private List<SlackDetailMessage> elements;
        private String type;

        public SlackContextMessageBuilder elements(List<SlackDetailMessage> elements) {
            this.elements = elements;
            return this;
        }

        public SlackContextMessageBuilder type(String type) {
            this.type = type;
            return this;
        }

        public SlackContextMessage build() {
            SlackContextMessage slackContextMessage = new SlackContextMessage();
            slackContextMessage.setElements(elements);
            slackContextMessage.setType(type);
            return slackContextMessage;
        }
    }
}
