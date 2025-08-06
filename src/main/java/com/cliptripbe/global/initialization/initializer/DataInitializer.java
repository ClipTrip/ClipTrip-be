package com.cliptripbe.global.initialization.initializer;

import static com.cliptripbe.global.initialization.type.DefaultData.STORAGE_SEOUL;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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
    private final BookmarkInitializer bookmarkInitializer;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (placeRepository.count() == 0) {
            List<Place> placeList = placeinitializer.registerPlace(STORAGE_SEOUL);
            bookmarkInitializer.initialBookmark(placeList, STORAGE_SEOUL);
        }
    }
}