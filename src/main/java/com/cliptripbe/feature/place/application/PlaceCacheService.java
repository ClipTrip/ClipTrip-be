package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import com.cliptripbe.infrastructure.port.cache.CacheServicePort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceCacheService {

    private final CacheServicePort cacheServicePort;

    public void cachePlace(
        List<PlaceDto> placeDtoList,
        Map<String, TranslationInfo> translatedPlacesMap,
        Language userLanguage
    ) {
        List<TranslatedPlaceCacheRequest> requests = placeDtoList.stream()
            .map(placeDto -> {
                String key = createTranslatedPlaceKey(placeDto, userLanguage);
                TranslationInfo translatedInfo = translatedPlacesMap.get(key);
                if (translatedInfo != null) {
                    return TranslatedPlaceCacheRequest.of(placeDto, translatedInfo, userLanguage);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

        if (!requests.isEmpty()) {
            cacheServicePort.cacheTranslatedPlaces(requests);
        }
    }

    // 키 생성 로직은 다음과 같이 헬퍼 메서드로 분리하는 것이 좋습니다.
    private String createTranslatedPlaceKey(PlaceDto placeDto, Language userLanguage) {
        return String.format("translatedPlace:%s:%s", placeDto.kakaoPlaceId(), userLanguage.name());
    }

    public Map<String, TranslationInfo> getAndFilter(List<PlaceDto> placeDtoList,
        Language userLanguage) {
        // 1. 모든 PlaceDto에 대한 캐시 키 리스트 생성
        List<String> keys = placeDtoList.stream()
            .map(dto -> createTranslatedPlaceKey(dto, userLanguage))
            .collect(Collectors.toList());

        // 2. Redis의 multiGet을 사용하여 한 번에 여러 데이터 조회
        List<TranslationInfo> values = cacheServicePort.multiGet(keys);

        // 3. 캐시된 데이터를 Map 형태로 변환 (키는 Redis 키, 값은 TranslationInfoWithId)
        Map<String, TranslationInfo> cachedTranslations = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            TranslationInfo value = values.get(i);
            if (value != null) {
                cachedTranslations.put(keys.get(i), value);
            }
        }
        return cachedTranslations;
    }
}
