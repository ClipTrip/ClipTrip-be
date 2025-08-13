package com.cliptripbe.infrastructure.port.cache;

import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import java.util.List;
import java.util.Optional;

public interface CacheServicePort {

    void cacheTranslatedPlaces(List<TranslatedPlaceCacheRequest> translatedPlaceCacheRequest);

    List<TranslationInfo> findAllByKeys(List<String> keys);

    Optional<TranslationInfo> retrieveByKey(String translatedPlaceKey);
}
