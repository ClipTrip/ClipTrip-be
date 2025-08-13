package com.cliptripbe.global.config;

import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
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

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, TranslationInfoDto> translationInfoRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, TranslationInfoDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<TranslationInfoDto> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
            objectMapper,
            TranslationInfoDto.class
        );

        redisTemplate.setValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }
}
