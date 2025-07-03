package com.cliptripbe.infrastructure.caption.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CaptionResponse(
    @JsonProperty("video_id") String videoId,
    @JsonProperty("captions") String captions
) {

}
