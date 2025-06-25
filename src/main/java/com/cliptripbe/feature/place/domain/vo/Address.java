package com.cliptripbe.feature.place.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
@Builder
public record Address(
    Double latitude,
    Double longitude,
    String zipCode,
    String roadAddress
) {

}
