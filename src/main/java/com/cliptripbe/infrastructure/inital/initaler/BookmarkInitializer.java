package com.cliptripbe.infrastructure.inital.initaler;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkInitializer {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;

    public void initialStorageBookmark(DefaultData defaultBookmark) {
        List<Place> placeList = placeRepository.findByRegionAndPlaceType(
            defaultBookmark.getRegion(),
            defaultBookmark.getPlaceType());
        Bookmark bookmark = Bookmark
            .builder()
            .name(defaultBookmark.getName())
            .description("서울에 존재하는 물품 보관함입니다.")
            .build();
        for (Place place : placeList) {
            BookmarkPlace bookmarkPlace = BookmarkPlace
                .builder()
                .bookmark(bookmark)
                .place(place)
                .build();
            bookmark.addBookmarkPlace(bookmarkPlace);
        }
        bookmarkRepository.save(bookmark);
    }

    public void initialStorageAccomdation(DefaultData defaultBookmark) {
        List<Place> placeList = placeRepository.findByRegionAndPlaceType(
            defaultBookmark.getRegion(),
            defaultBookmark.getPlaceType()
        );
        Bookmark bookmark = Bookmark
            .builder()
            .name(defaultBookmark.getName())
            .description("서울에 존재하는 장애인 관광객을 위한 숙박장소.")
            .build();
        for (Place place : placeList) {
            BookmarkPlace bookmarkPlace = BookmarkPlace
                .builder()
                .bookmark(bookmark)
                .place(place)
                .build();
            bookmark.addBookmarkPlace(bookmarkPlace);
        }
        bookmarkRepository.save(bookmark);
    }
}
