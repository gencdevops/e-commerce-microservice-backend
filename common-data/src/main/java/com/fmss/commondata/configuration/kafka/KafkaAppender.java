package com.fmss.commondata.configuration.kafka;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class KafkaAppender<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {
    private static final String KAFKA_LOGGER_PREFIX = "org.apache.kafka.clients";
    private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<>();
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();

    protected String topic = null;
    protected LayoutKafkaMessageEncoder<E> encoder = null;
    protected static final Set<String> KNOWN_PRODUCER_CONFIG_KEYS = new HashSet<>();
    protected Map<String, Object> producerConfig = new HashMap<>();

    public KafkaAppender() {
        this.addProducerConfigValue("key.serializer", ByteArraySerializer.class.getName());
        this.addProducerConfigValue("value.serializer", ByteArraySerializer.class.getName());
    }

    @Override
    public void doAppend(E e) {
        Object event;
        while ((event = this.queue.poll()) != null) {
            super.doAppend((E) event);
        }
        if (e instanceof ILoggingEvent && ((ILoggingEvent) e).getLoggerName().startsWith(KAFKA_LOGGER_PREFIX)) {
            this.queue.add(e);
        } else {
            super.doAppend(e);
        }
    }

    @Override
    public void start() {
        if (this.checkPrerequisites()) {
            super.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    public void addAppender(Appender<E> newAppender) {
        this.aai.addAppender(newAppender);
    }

    public Iterator<Appender<E>> iteratorForAppenders() {
        return this.aai.iteratorForAppenders();
    }

    public Appender<E> getAppender(String name) {
        return this.aai.getAppender(name);
    }

    public boolean isAttached(Appender<E> appender) {
        return this.aai.isAttached(appender);
    }

    public void detachAndStopAllAppenders() {
        this.aai.detachAndStopAllAppenders();
    }

    public boolean detachAppender(Appender<E> appender) {
        return this.aai.detachAppender(appender);
    }

    public boolean detachAppender(String name) {
        return this.aai.detachAppender(name);
    }
    @SneakyThrows
    protected void append(E e) {
        byte[] payload = this.encoder.doEncode(e);
        ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(this.topic, null, payload);



        try {
            final Future<RecordMetadata> future = createProducer().send(producerRecord);
            future.get();
        } catch (BufferExhaustedException | ExecutionException | CancellationException ex) {
            onFailedDelivery(e);
        }
    }

    protected Producer<byte[], byte[]> createProducer() {
        return new KafkaProducer<>(new HashMap<>(this.producerConfig));
    }

    protected boolean checkPrerequisites() {
        boolean errorFree = true;
        if (this.producerConfig.get("bootstrap.servers") == null) {
            this.addError("No \"bootstrap.servers\" set for the appender named [\"" + this.name + "\"].");
            errorFree = false;
        }

        if (this.topic == null) {
            this.addError("No topic set for the appender named [\"" + this.name + "\"].");
            errorFree = false;
        }

        if (this.encoder == null) {
            this.addError("No encoder set for the appender named [\"" + this.name + "\"].");
            errorFree = false;
        }

        return errorFree;
    }

    public void setEncoder(LayoutKafkaMessageEncoder<E> layout) {
        this.encoder = layout;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void addProducerConfig(String keyValue) {
        String[] split = keyValue.split("=", 2);
        if (split.length == 2) {
            this.addProducerConfigValue(split[0], split[1]);
        }

    }

    public void addProducerConfigValue(String key, Object value) {
        if (!KNOWN_PRODUCER_CONFIG_KEYS.contains(key)) {
            this.addWarn("The key \"" + key + "\" is now a known kafka producer config key.");
        }

        this.producerConfig.put(key, value);
    }

    public Map<String, Object> getProducerConfig() {
        return this.producerConfig;
    }


    public void onFailedDelivery(E evt) {
        KafkaAppender.this.aai.appendLoopOnAppenders(evt);
    }

    static {
        KNOWN_PRODUCER_CONFIG_KEYS.add("bootstrap.servers");
        KNOWN_PRODUCER_CONFIG_KEYS.add("metadata.fetch.timeout.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("metadata.max.age.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("batch.size");
        KNOWN_PRODUCER_CONFIG_KEYS.add("buffer.memory");
        KNOWN_PRODUCER_CONFIG_KEYS.add("acks");
        KNOWN_PRODUCER_CONFIG_KEYS.add("timeout.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("linger.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("client.id");
        KNOWN_PRODUCER_CONFIG_KEYS.add("send.buffer.bytes");
        KNOWN_PRODUCER_CONFIG_KEYS.add("receive.buffer.bytes");
        KNOWN_PRODUCER_CONFIG_KEYS.add("max.request.size");
        KNOWN_PRODUCER_CONFIG_KEYS.add("reconnect.backoff.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("block.on.buffer.full");
        KNOWN_PRODUCER_CONFIG_KEYS.add("retries");
        KNOWN_PRODUCER_CONFIG_KEYS.add("retry.backoff.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("compression.type");
        KNOWN_PRODUCER_CONFIG_KEYS.add("metrics.sample.window.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("metrics.num.samples");
        KNOWN_PRODUCER_CONFIG_KEYS.add("metric.reporters");
        KNOWN_PRODUCER_CONFIG_KEYS.add("max.in.flight.requests.per.connection");
        KNOWN_PRODUCER_CONFIG_KEYS.add("key.serializer");
        KNOWN_PRODUCER_CONFIG_KEYS.add("value.serializer");
        KNOWN_PRODUCER_CONFIG_KEYS.add("connections.max.idle.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("partitioner.class");
        KNOWN_PRODUCER_CONFIG_KEYS.add("max.block.ms");
        KNOWN_PRODUCER_CONFIG_KEYS.add("request.timeout.ms");
    }
}
