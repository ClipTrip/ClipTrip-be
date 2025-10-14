package com.cliptripbe.feature.place.application;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
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
                List<Long> bookmarkIds = bookmarkIdsMap.getOrDefault(placeDto.kakaoPlaceId(), List.of());
                return PlaceListResponse.ofKorean(placeDto, bookmarkIds);
            })
            .toList();
    }
}