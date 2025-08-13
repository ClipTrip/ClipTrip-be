package com.cliptripbe.infrastructure.adapter.out.cache;

import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import com.cliptripbe.infrastructure.port.cache.CacheServicePort;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheAdapter implements CacheServicePort {

    private final RedisTemplate<String, TranslationInfoDto> translationInfoRedisTemplate;
    private final static Integer TIME_OUT = 7;

    @Override
    public void cacheTranslatedPlaces(
        List<TranslatedPlaceCacheRequest> translatedPlaceCacheRequest) {
        if (translatedPlaceCacheRequest == null || translatedPlaceCacheRequest.isEmpty()) {
            return;
        }

        for (TranslatedPlaceCacheRequest request : translatedPlaceCacheRequest) {
            if (request == null) {
                continue;
            }
            String key = request.key();
            TranslationInfoDto value = request.translationInfoDto();
            if (key == null || key.isBlank() || value == null) {
                continue;
            }
            translationInfoRedisTemplate.opsForValue().set(key, value, 7, TimeUnit.DAYS);
        }
    }

    @Override
    public List<TranslationInfoDto> findAllByKeys(List<String> keys) {
        return translationInfoRedisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Optional<TranslationInfoDto> retrieveByKey(String translatedPlaceKey) {
        TranslationInfoDto result = translationInfoRedisTemplate.opsForValue().get(translatedPlaceKey);
        return Optional.ofNullable(result);
    }
}