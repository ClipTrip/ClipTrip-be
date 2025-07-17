package com.cliptripbe.infrastructure.inital.initaler;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkInitializer {

    private final BookmarkRepository bookmarkRepository;

    public void initialBookmark(List<Place> placeList, DefaultData defaultData) {
        Bookmark bookmark = Bookmark.createDefault(defaultData.getName(),
            defaultData.getDescription());
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
