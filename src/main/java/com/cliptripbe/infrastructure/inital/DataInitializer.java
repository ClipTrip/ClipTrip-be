package com.cliptripbe.infrastructure.inital;

import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.inital.initaler.BookmarkInitializer;
import com.cliptripbe.infrastructure.inital.initaler.PlaceInitializer;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PlaceInitializer placeinitializer;

    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkInitializer bookmarkInitializer;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (placeRepository.count() == 0) {
            placeinitializer.registerCulturePlace();
        }

        if (DefaultData.STORAGE_SEOUL.notExistsIn(bookmarkRepository)) {
            placeinitializer.registerStoragePlace();
            bookmarkInitializer.initialStorageBookmark(DefaultData.STORAGE_SEOUL);
        }

        if (DefaultData.ACCOMMODATION_SEOUL.notExistsIn(bookmarkRepository)) {
            placeinitializer.registerAccomodationPlace();
            bookmarkInitializer.initialStorageAccomdation(DefaultData.ACCOMMODATION_SEOUL);
        }
    }
}