package com.cliptripbe.global.config;

import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, TranslationInfo> translationInfoRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, TranslationInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<TranslationInfo> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
            objectMapper,
            TranslationInfo.class
        );

        redisTemplate.setValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }
}
