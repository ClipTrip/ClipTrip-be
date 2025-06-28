package com.cliptripbe.feature.bookmark.api.dto.request;

import com.cliptripbe.feature.place.domain.vo.PlaceVO;
import java.util.List;

public record CreateBookmarkRequestDto(
    String bookmarkName,
    String description,
    List<PlaceVO> placeVOList
) {

}
