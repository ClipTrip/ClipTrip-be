package com.cliptripbe.feature.place.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
public record Address(
    Double latitude,
    Double longitude,
    String roadAddress
) {

}
