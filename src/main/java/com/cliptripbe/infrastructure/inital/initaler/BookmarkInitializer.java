package com.cliptripbe.infrastructure.inital.initaler;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkInitializer {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;

    public void initialStorageBookmark(String region, PlaceType placeType) {
        List<Place> placeList = placeRepository.findByRegionAndPlaceType(region,
            placeType);
        Bookmark bookmark = Bookmark
            .builder()
            .name("물품 보관함(서울)")
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
}
