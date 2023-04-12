package com.fmss.orderservice.model.slack;

public class SlackDetailMessage {
    private String text;
    private String type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static SlackMessageBuilder builder() {
        return new SlackMessageBuilder();
    }

    public static final class SlackMessageBuilder {
        private String text;
        private String type;

        public SlackMessageBuilder text(String text) {
            this.text = text;
            return this;
        }

        public SlackMessageBuilder type(String type) {
            this.type = type;
            return this;
        }

        public SlackDetailMessage build() {
            SlackDetailMessage slackMessage = new SlackDetailMessage();
            slackMessage.setText(text);
            slackMessage.setType(type);
            return slackMessage;
        }
    }
}
