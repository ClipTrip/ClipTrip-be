package com.cliptripbe.infrastructure.kakao.dto;

import java.util.List;

public record KakaoMapResponse(
    Meta meta,
    List<Document> documents
) {

    public record Meta(
        int total_count,
        int pageable_count,
        boolean is_end
    ) {

    }

    public record Document(
        String place_name,
        String address_name,
        String road_address_name,
        String id,
        String phone,
        String category_name,
        String x,
        String y
    ) {

    }
}
