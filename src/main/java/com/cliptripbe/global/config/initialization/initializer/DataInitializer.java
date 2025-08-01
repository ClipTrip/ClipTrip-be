package com.cliptripbe.global.config.initialization.initializer;

import static com.cliptripbe.global.config.initialization.type.DefaultData.ACCOMMODATION_SEOUL;
import static com.cliptripbe.global.config.initialization.type.DefaultData.BUSAN_ACCESSIBLE_TOURISM;
import static com.cliptripbe.global.config.initialization.type.DefaultData.INCHEON_ACCESSIBLE_TOURISM;
import static com.cliptripbe.global.config.initialization.type.DefaultData.SOKCHO_OPEN_TOURISM;
import static com.cliptripbe.global.config.initialization.type.DefaultData.STORAGE_SEOUL;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.infrastructure.PlaceRepository;
import com.cliptripbe.global.config.initialization.type.DefaultData;
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
            placeinitializer.registerPlace(DefaultData.BF_CULTURE_TOURISM);
            List<Place> placeList = placeinitializer.registerPlace(STORAGE_SEOUL);
            bookmarkInitializer.initialBookmark(placeList, STORAGE_SEOUL);

            placeList = placeinitializer.registerPlace(ACCOMMODATION_SEOUL);
            bookmarkInitializer.initialBookmark(placeList, ACCOMMODATION_SEOUL);

            List<DefaultData> accessibleTourismList = List.of(
                INCHEON_ACCESSIBLE_TOURISM,
                SOKCHO_OPEN_TOURISM,
                BUSAN_ACCESSIBLE_TOURISM
            );

            for (DefaultData data : accessibleTourismList) {
                List<Place> pl = placeinitializer.registerFourCoulmn(data);
                bookmarkInitializer.initialBookmark(pl, data);
            }
        }
    }
}