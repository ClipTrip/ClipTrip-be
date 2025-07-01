package com.cliptripbe.feature.bookmark.api.dto.request;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import java.util.List;

public record UpdateBookmarkRequestDto(
    String bookmarkName,
    String description,
    List<PlaceInfoRequestDto> placeInfoRequestDtos

) {

}
