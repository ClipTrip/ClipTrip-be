package com.cliptripbe.feature.place.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record Address(
    Double latitude,
    Double longitude,
    String roadAddress
) {

}
