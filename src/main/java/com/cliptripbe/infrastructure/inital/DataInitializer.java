package com.cliptripbe.infrastructure.inital;

import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.infrastructure.inital.initaler.BookmarkInitializer;
import com.cliptripbe.infrastructure.inital.initaler.PlaceInitializer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
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
            placeinitializer.registerStoragePlace();
        }
        if (bookmarkRepository.findByName("물품 보관함(서울)").isEmpty()) {
            bookmarkInitializer.initialStorageBookmark("서울", PlaceType.LUGGAGE_STORAGE);
        }
    }
}