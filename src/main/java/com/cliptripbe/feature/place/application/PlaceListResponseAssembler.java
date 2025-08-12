package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PlaceListResponseAssembler {

    public List<PlaceListResponse> createPlaceListResponseForKorean(
        List<PlaceDto> placeDtoList,
        Map<String, List<Long>> bookmarkIdsMap
    ) {
        return placeDtoList.stream()
            .map(placeDto -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(placeDto.kakaoPlaceId(), List.of());
                return PlaceListResponse.ofKorean(placeDto, bookmarkIds);
            })
            .toList();
    }

    public List<PlaceListResponse> createPlaceListResponseForForeign(
        Map<String, PlaceDto> placeDtoMap,
        List<TranslatedPlaceAddress> translatedPlaces,
        Map<String, List<Long>> bookmarkIdsMap,
        Language userLanguage
    ) {

        return translatedPlaces.stream()
            .map(tp -> {
                PlaceDto placeDto = getMatchPlaceDto(tp, placeDtoMap);
                String kakaoPlaceId = placeDto.kakaoPlaceId();
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(kakaoPlaceId, List.of());
                return PlaceListResponse.ofForeign(placeDto, tp, bookmarkIds, userLanguage);
            })
            .toList();
    }

    private PlaceDto getMatchPlaceDto(TranslatedPlaceAddress tp, Map<String, PlaceDto> placeDtoMap) {
        String key = tp.placeName() + tp.roadAddress();
        return placeDtoMap.get(key);
    }
}