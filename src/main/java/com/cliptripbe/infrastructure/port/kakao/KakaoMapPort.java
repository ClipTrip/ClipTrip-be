package com.cliptripbe.infrastructure.port.kakao;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import java.util.List;

public interface KakaoMapPort {

    List<PlaceDto> searchPlacesByCategory(PlaceSearchByCategoryRequest request);

    List<PlaceDto> searchPlacesByKeyWord(PlaceSearchByKeywordRequest request);

    List<PlaceDto> searchFirstPlacesInParallel(List<String> keywords);
}
