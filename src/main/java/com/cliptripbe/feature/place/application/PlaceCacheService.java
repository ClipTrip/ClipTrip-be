package com.cliptripbe.feature.place.application;

import static com.cliptripbe.global.util.CacheUtils.createTranslatedPlaceKey;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.translate.dto.response.TranslationSplitResult;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.util.CacheUtils;
import com.cliptripbe.infrastructure.adapter.out.cache.dto.TranslatedPlaceCacheRequest;
import com.cliptripbe.infrastructure.port.cache.CacheServicePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceCacheService {

    private final CacheServicePort cacheServicePort;

    public void cachePlace(
        List<TranslatedPlaceAddress> translatedPlaceAddresses,
        Language userLanguage
    ) {
        if (translatedPlaceAddresses == null) {
            return;
        }

        List<TranslatedPlaceCacheRequest> requests = new ArrayList<>();
        for (int i = 0; i < translatedPlaceAddresses.size(); i++) {
            requests.add(
                TranslatedPlaceCacheRequest.of(translatedPlaceAddresses.get(i), userLanguage));
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
            .map(dto -> createTranslatedPlaceKey(dto.placeName(), dto.roadAddress(), language))
            .toList();

        List<TranslationInfoDto> translationInfoDtos = cacheServicePort.findAllByKeys(redisKeys);

        List<PlaceDto> untranslatedPlaces = new ArrayList<>();
        List<TranslatedPlaceAddress> translatedPlaceInfos = new ArrayList<>();

        for (int i = 0; i < placeDtoList.size(); i++) {
            PlaceDto placeDto = placeDtoList.get(i);
            TranslationInfoDto translationInfoDto = translationInfoDtos.get(i);

            if (translationInfoDto != null) {
                TranslatedPlaceAddress translatedPlaceAddress = TranslatedPlaceAddress.of(placeDto, translationInfoDto,
                    language);
                translatedPlaceInfos.add(translatedPlaceAddress);
            } else {
                untranslatedPlaces.add(placeDto);
            }
        }
        return new TranslationSplitResult(translatedPlaceInfos, untranslatedPlaces);
    }

    public Optional<TranslationInfoDto> getTranslationInfo(Place place, Language language) {
        String translatedPlaceKey = CacheUtils.createTranslatedPlaceKey(place.getName(),
            place.getAddress().roadAddress(), language);
        return cacheServicePort.retrieveByKey(translatedPlaceKey);
    }
}
