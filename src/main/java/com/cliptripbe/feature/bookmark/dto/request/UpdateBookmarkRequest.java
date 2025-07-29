package com.cliptripbe.feature.bookmark.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import java.util.List;

public record UpdateBookmarkRequest(
    String bookmarkName,
    String description,
    List<PlaceInfoRequest> placeInfoRequests

) {

}
