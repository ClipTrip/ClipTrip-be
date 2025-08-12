package com.cliptripbe.feature.place.dto.response;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.feature.translate.dto.response.TranslatedPlaceAddress;
import com.cliptripbe.feature.translate.dto.response.TranslationInfo;
import com.cliptripbe.feature.user.domain.type.Language;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Optional;
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
    Optional<String> translatedPlaceName,
    Optional<String> translatedRoadAddress,
    Language language,
    String kakaoPlaceId,
    List<Long> bookmarkedIdList
) {

    public static PlaceListResponse ofDto(PlaceDto placeDto, Language language,
        List<Long> bookmarkedIdList) {
        return PlaceListResponse.builder()
            .placeName(placeDto.placeName())
            .roadAddress(placeDto.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .translatedPlaceName(Optional.empty())
            .translatedRoadAddress(Optional.empty())
            .language(language)
            .bookmarkedIdList(bookmarkedIdList)
            .build();
    }

    public static PlaceListResponse ofDto(
        PlaceDto placeDto, TranslatedPlaceAddress tp, List<Long> bookmarkIds, Language language
    ) {
        List<Long> safeBookmarkedIdList = bookmarkIds == null || bookmarkIds.isEmpty()
            ? List.of() : bookmarkIds;

        return PlaceListResponse.builder()
            .placeName(tp.placeName())
            .roadAddress(tp.roadAddress())
            .phone(placeDto.phone())
            .type(PlaceType.findByCode(placeDto.categoryCode()))
            .longitude(placeDto.longitude())
            .latitude(placeDto.latitude())
            .kakaoPlaceId(placeDto.kakaoPlaceId())
            .translatedPlaceName(Optional.ofNullable(tp.translationInfo().translatedName()))
            .translatedRoadAddress(Optional.ofNullable(tp.translationInfo().translatedRoadAddress()))
            .language(language)
            .bookmarkedIdList(safeBookmarkedIdList)
            .build();
    }

    public static PlaceListResponse ofEntity(
        Place place,
        TranslationInfo translatedInfo,
        Language language,
        List<Long> bookmarkedIdList
    ) {
        Optional<String> translatedPlaceName = Optional.ofNullable(
            translatedInfo != null ? translatedInfo.translatedName() : null
        );
        Optional<String> translatedRoadAddress = Optional.ofNullable(
            translatedInfo != null ? translatedInfo.translatedRoadAddress() : null
        );
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
            .translatedPlaceName(translatedPlaceName)
            .translatedRoadAddress(translatedRoadAddress)
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

    public static PlaceListResponse ofTranslation(
        Place place,
        PlaceTranslation placeTranslation,
        Integer placeOrder
    ) {
        String placeName = placeTranslation != null && placeTranslation.getName() != null
            ? placeTranslation.getName()
            : place.getName();

        String roadAddress = placeTranslation != null && placeTranslation.getRoadAddress() != null
            ? placeTranslation.getRoadAddress()
            : place.getAddress().roadAddress();

        return PlaceListResponse.builder()
            .placeId(place.getId())
            .placeName(placeName)
            .roadAddress(roadAddress)
            .phone(place.getPhoneNumber())
            .type(place.getPlaceType())
            .longitude(place.getAddress().longitude())
            .latitude(place.getAddress().latitude())
            .placeOrder(placeOrder)
            .build();
    }

    public static PlaceListResponse ofKorean(PlaceDto placeDto, List<Long> bookmarkIds) {
        return ofDto(placeDto, Language.KOREAN, bookmarkIds);
    }

    public static PlaceListResponse ofForeign(PlaceDto placeDto, TranslatedPlaceAddress tp, List<Long> bookmarkIds,
        Language userLanguage) {
        return ofDto(placeDto, tp, bookmarkIds, userLanguage);
    }
}
