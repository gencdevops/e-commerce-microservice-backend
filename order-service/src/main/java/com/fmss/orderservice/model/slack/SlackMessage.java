package com.fmss.orderservice.model.slack;

public class SlackMessage {
    private SlackDetailMessage text;
    private String type;

    public SlackDetailMessage getText() {
        return text;
    }

    public void setText(SlackDetailMessage text) {
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
        private SlackDetailMessage text;
        private String type;

        public SlackMessageBuilder text(SlackDetailMessage text) {
            this.text = text;
            return this;
        }

        public SlackMessageBuilder type(String type) {
            this.type = type;
            return this;
        }

        public SlackMessage build() {
            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setText(text);
            slackMessage.setType(type);
            return slackMessage;
        }
    }
}
