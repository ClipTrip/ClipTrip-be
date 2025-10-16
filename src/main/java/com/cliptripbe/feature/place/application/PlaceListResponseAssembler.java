package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PlaceListResponseAssembler {

    public static List<PlaceListResponse> createPlaceListResponseForKorean(
        List<PlaceDto> placeDtoList,
        Map<String, List<Long>> bookmarkIdsMap
    ) {
        return placeDtoList.stream()
            .map(placeDto -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(placeDto.kakaoPlaceId(),
                    List.of());
                return PlaceListResponse.ofKorean(placeDto, bookmarkIds);
            })
            .toList();
    }

    public static List<PlaceListResponse> createPlaceListResponseForLuggage(
        List<Place> placeList,
        Map<Long, List<Long>> bookmarkIdsMap,
        User user
    ) {
        return placeList.stream()
            .map(place -> {
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(place.getId(), List.of());
                return PlaceListResponse.ofEntity(place, user.getLanguage(), bookmarkIds);
            })
            .toList();
    }
}