package com.cliptripbe.infrastructure.port.cache;

import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import java.util.List;
import java.util.Optional;

public interface CacheServicePort {

    void cacheTranslatedPlaces(List<TranslatedPlaceCacheRequest> translatedPlaceCacheRequest);

    List<TranslationInfoDto> findAllByKeys(List<String> keys);

    Optional<TranslationInfoDto> retrieveByKey(String translatedPlaceKey);
}
