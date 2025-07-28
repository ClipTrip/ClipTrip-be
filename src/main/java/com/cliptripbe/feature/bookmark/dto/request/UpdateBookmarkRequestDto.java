package com.cliptripbe.feature.bookmark.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequestDto;
import java.util.List;

public record UpdateBookmarkRequestDto(
    String bookmarkName,
    String description,
    List<PlaceInfoRequestDto> placeInfoRequestDtos

) {

}
