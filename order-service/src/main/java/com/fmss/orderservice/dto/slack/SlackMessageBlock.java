package com.fmss.orderservice.dto.slack;

import java.util.List;

public class SlackMessageBlock {
    private List<Object> blocks;

    public List<Object> getBlocks() {
        return blocks;
    }

    public static SlackMessageBlockBuilder builder() {
        return new SlackMessageBlockBuilder();
    }

    public static final class SlackMessageBlockBuilder {
        private List<Object> blocks;

        public SlackMessageBlockBuilder blocks(List<Object> blocks) {
            this.blocks = blocks;
            return this;
        }

        public SlackMessageBlock build() {
            SlackMessageBlock slackMessageBlock = new SlackMessageBlock();
            slackMessageBlock.blocks = this.blocks;
            return slackMessageBlock;
        }
    }
}

