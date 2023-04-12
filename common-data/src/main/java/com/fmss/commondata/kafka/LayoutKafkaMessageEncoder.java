package com.fmss.commondata.kafka;

import ch.qos.logback.core.Layout;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A KafkaMessageEncoder that can be configured with a {@link Layout} and a {@link Charset} and creates
 * a serialized string for each event using the given layout.
 *
 * @since 0.1.0
 */
public class LayoutKafkaMessageEncoder<E> extends ContextAwareBase implements LifeCycle {

    public LayoutKafkaMessageEncoder() {
    }

    public LayoutKafkaMessageEncoder(Layout<E> layout, Charset charset) {
        this.layout = layout;
        this.charset = charset;
    }

    private Layout<E> layout;
    private Charset charset;
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private boolean started = false;


    @Override
    public void start() {
        if (charset == null) {
            addInfo("No charset specified for PatternLayoutKafkaEncoder. Using default UTF8 encoding.");
            charset = UTF8;
        }
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }


    public byte[] doEncode(E event) {
        final String message = layout.doLayout(event);
        return message.getBytes(charset);
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Layout<E> getLayout() {
        return layout;
    }

    public Charset getCharset() {
        return charset;
    }
}
