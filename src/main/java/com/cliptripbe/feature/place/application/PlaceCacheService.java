package com.cliptripbe.feature.place.application;

import static com.cliptripbe.global.util.CacheUtils.createTranslatedPlaceKey;

import com.cliptripbe.feature.place.domain.vo.PlaceInfoWithTranslation;
import com.cliptripbe.feature.place.domain.vo.TranslationInfo;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.translate.dto.TranslationSplitResult;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import com.cliptripbe.infrastructure.port.cache.CacheServicePort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceCacheService {


    private final CacheServicePort cacheServicePort;


    public void cachePlace(
        List<PlaceDto> placeDtoList,
        List<TranslationInfo> translationInfos,
        Language userLanguage
    ) {
        List<TranslatedPlaceCacheRequest> requests = new ArrayList<>();
        for (int i = 0; i < placeDtoList.size(); i++) {
            requests.add(
                TranslatedPlaceCacheRequest.of(placeDtoList.get(i), translationInfos.get(i),
                    userLanguage));
        }
        if (!requests.isEmpty()) {
            cacheServicePort.cacheTranslatedPlaces(requests);
        }
    }


    public TranslationSplitResult classifyPlaces(
        List<PlaceDto> placeDtoList,
        Language language
    ) {
        List<String> redisKeys = placeDtoList.stream()
            .map(dto -> createTranslatedPlaceKey(dto, language))
            .toList();

        List<TranslationInfo> translationInfos = cacheServicePort.multiGet(redisKeys);

        List<PlaceDto> untranslatedPlaces = new ArrayList<>();
        List<PlaceInfoWithTranslation> translatedPlaceInfos = new ArrayList<>();

        for (int i = 0; i < placeDtoList.size(); i++) {
            PlaceDto placeDto = placeDtoList.get(i);
            TranslationInfo translationInfo = translationInfos.get(i);

            if (translationInfo != null) {
                // 번역 정보가 있는 경우
                translatedPlaceInfos.add(
                    PlaceInfoWithTranslation.of(placeDto, translationInfo, language)
                );
            } else {
                // 번역 정보가 없는 경우
                untranslatedPlaces.add(placeDto);
            }
        }

        return new TranslationSplitResult(translatedPlaceInfos, untranslatedPlaces);
    }
}
