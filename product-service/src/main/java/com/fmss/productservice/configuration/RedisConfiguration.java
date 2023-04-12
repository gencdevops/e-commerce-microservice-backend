package com.fmss.productservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {


    @Value("${redis.server.address:}")
    private String redisServerAddress;
    @Value("${redis.server.port}")
    private int redisServerPort;
    @Value("${redis.server.password}")
    private String redisServerPassword;
    @Value("${redis.server.environment}")
    private String redisServerEnvironment;
    @Value("${redis.server.cluster.nodes:}")
    private List<String> redisServerClusterNodes;
    @Value("${redis.service.prefix}")
    private String redisServicePrefix;
    @Value("${redis.service.cacheable.ttl}")
    private long redisCacheableTtl;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("localhost", 6379);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .transactionAware()
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.java());
        template.setHashKeySerializer(RedisSerializer.json());
        return template;
    }


    private RedisStandaloneConfiguration localRedisConnection() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        return redisStandaloneConfiguration;
    }

    private RedisClusterConfiguration openshiftRedisConnection() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        if (this.redisServerClusterNodes != null && !this.redisServerClusterNodes.isEmpty()) {
            redisClusterConfiguration = new RedisClusterConfiguration(this.redisServerClusterNodes);
        } else {
            redisClusterConfiguration.addClusterNode(new RedisNode(this.redisServerAddress, this.redisServerPort));
        }

        redisClusterConfiguration.setPassword(RedisPassword.of(this.redisServerPassword));
        return redisClusterConfiguration;
    }

    @Bean
    RedisCacheWriter redisCacheWriter() {
        return RedisCacheWriter.lockingRedisCacheWriter(this.redisConnectionFactory());
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ProductService productService){
//        return args -> {
////            productService.createProduct(new ProductRequestDto("test1", BigDecimal.ONE, true));
////            productService.createProduct(new ProductRequestDto("test2", BigDecimal.TEN, true));
////            productService.createProduct(new ProductRequestDto("test3", BigDecimal.ZERO, true));
////
//            productService.getAllProducts();
//            var allProducts = (List<ProductResponseDto>) redisTemplate().opsForValue().get("allProducts");
//
//            System.out.println();
//        };
//    }


}
