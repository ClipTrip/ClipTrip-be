package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.user.domain.type.Language;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlaceListResponse(
    Long placeId,
    String placeName,
    String roadAddress,
    String phone,
    PlaceType type,
    double longitude,
    double latitude,
    Integer placeOrder,
    Language language,
    String kakaoPlaceId,
    List<Long> bookmarkedIdList
) {

    public static PlaceListResponse ofDto(
        PlaceDto placeDto,
        Language language,
        List<Long> bookmarkedIdList
    ) {
        return PlaceListResponse.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .language(language)
            .bookmarkedIdList(bookmarkedIdList)
            .build();
    }

    public static PlaceListResponse ofDto(
        PlaceDto placeDto, List<Long> bookmarkIds, Language language
    ) {
        List<Long> safeBookmarkedIdList = bookmarkIds == null || bookmarkIds.isEmpty()
            ? List.of() : bookmarkIds;

        return PlaceListResponse.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .language(language)
            .bookmarkedIdList(safeBookmarkedIdList)
            .build();
    }

    public static PlaceListResponse ofEntity(
        Place place,
        Language language,
        List<Long> bookmarkedIdList
    ) {
        if (bookmarkedIdList == null || bookmarkedIdList.isEmpty()) {
            bookmarkedIdList = List.of();
        }
        return PlaceListResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .kakaoPlaceId(place.getKakaoPlaceId())
            .language(language)
            .bookmarkedIdList(bookmarkedIdList)
            .build();
    }

    public static PlaceListResponse fromEntity(Place place, Integer placeOrder) {
        String kakaoPlaceId = place.getKakaoPlaceId();
        if (kakaoPlaceId == null) {
            kakaoPlaceId = "-1";
        }
        return PlaceListResponse.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .roadAddress(place.getAddress().roadAddress())
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .placeOrder(placeOrder)
            .kakaoPlaceId(kakaoPlaceId)
            .build();
    }

    public static PlaceListResponse ofKorean(PlaceDto placeDto, List<Long> bookmarkIds) {
        return ofDto(placeDto, Language.KOREAN, bookmarkIds);
    }
}
