package com.cliptripbe.infrastructure.adapter.out.cache;

import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import com.cliptripbe.infrastructure.port.cache.CacheServicePort;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheAdapter implements CacheServicePort {

    private final RedisTemplate<String, TranslationInfo> translationInfoRedisTemplate;
    private final static Integer TIME_OUT = 7;

    @Override
    public void cacheTranslatedPlaces(
        List<TranslatedPlaceCacheRequest> translatedPlaceCacheRequest) {
        if (translatedPlaceCacheRequest == null || translatedPlaceCacheRequest.isEmpty()) {
            return;
        }

        for (TranslatedPlaceCacheRequest request : translatedPlaceCacheRequest) {
            String key = request.key();
            TranslationInfo value = request.translationInfo();

            // Redis에 key-value 쌍을 저장
            translationInfoRedisTemplate.opsForValue().set(key, value);

            // 만료 시간 설정
            translationInfoRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
        }
    }

    @Override
    public List<TranslationInfo> multiGet(List<String> keys) {
        return translationInfoRedisTemplate.opsForValue().multiGet(keys);
    }
}