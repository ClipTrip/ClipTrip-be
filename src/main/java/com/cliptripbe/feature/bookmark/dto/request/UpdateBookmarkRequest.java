package com.cliptripbe.feature.bookmark.dto.request;

import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import jakarta.validation.Valid;
import java.util.List;

public record UpdateBookmarkRequest(
    String bookmarkName,
    String description,
    List<@Valid PlaceInfoRequest> placeInfoRequests

) {

}
