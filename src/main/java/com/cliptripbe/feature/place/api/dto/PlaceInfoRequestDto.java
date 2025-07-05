package com.cliptripbe.feature.place.api.dto;

import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.domain.vo.Address;
import jakarta.validation.constraints.Pattern;

public record PlaceInfoRequestDto(
    Address address,
    String placeName,
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
    String phoneNumber,
    PlaceType type
    
) {

}
