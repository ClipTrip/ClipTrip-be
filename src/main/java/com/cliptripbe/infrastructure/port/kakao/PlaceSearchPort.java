package com.cliptripbe.infrastructure.port.kakao;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByCategoryRequest;
import com.cliptripbe.feature.place.dto.request.PlaceSearchByKeywordRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlaceSearchPort {

    List<PlaceDto> searchPlacesByCategory(PlaceSearchByCategoryRequest request);

    List<PlaceDto> searchPlacesByKeyWord(PlaceSearchByKeywordRequest request);

    CompletableFuture<List<PlaceDto>> searchFirstPlacesInParallelAsync(List<String> keywords);
}
