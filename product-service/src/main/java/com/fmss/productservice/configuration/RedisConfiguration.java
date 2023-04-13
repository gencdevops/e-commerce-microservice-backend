package com.fmss.productservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

@EnableCaching
@Configuration
public class RedisConfiguration {

    @Value(value = "${redis.server.address:}")
    private String redisServerAddress;

    @Value(value = "${redis.server.port}")
    private int redisServerPort;



    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(localRedisConnection());
    }

    @Bean("fmssRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    private RedisStandaloneConfiguration localRedisConnection(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(redisServerPort);
        return redisStandaloneConfiguration;
    }


}