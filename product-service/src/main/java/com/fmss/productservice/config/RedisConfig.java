package com.fmss.productservice.config;

import com.fmss.productservice.model.dto.ProductRequestDto;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("localhost", 6379);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ProductService productService){
        return args -> {
            productService.createProduct(new ProductRequestDto("test1", BigDecimal.ONE, true));
            productService.createProduct(new ProductRequestDto("test2", BigDecimal.TEN, true));
            productService.createProduct(new ProductRequestDto("test3", BigDecimal.ZERO, true));

            productService.getAllProducts();
            var allProducts = (List<ProductResponseDto>) redisTemplate().opsForValue().get("allProducts");

            System.out.println();
        };
    }


}
