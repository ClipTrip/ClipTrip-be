package com.cliptripbe.infrastructure.port.cache;

import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import java.util.List;

public interface CacheServicePort {

    void cacheTranslatedPlaces(List<TranslatedPlaceCacheRequest> translatedPlaceCacheRequest);

    List<TranslationInfo> multiGet(List<String> keys);
}
