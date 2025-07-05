package com.cliptripbe.infrastructure.inital.type;


import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultData {
    STORAGE_SEOUL("물품 보관함(서울)", PlaceType.LUGGAGE_STORAGE, "서울"),
    ACCOMMODATION_SEOUL("장애인 관광객을 위한 숙박 북마크(서울)", PlaceType.ACCOMMODATION, "서울");

    private final String name;
    private final PlaceType placeType;
    private final String region;

    public boolean notExistsIn(BookmarkRepository bookmarkRepository) {
        return bookmarkRepository.findByName(name).isEmpty();
    }
}