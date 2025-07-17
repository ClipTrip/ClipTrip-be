package com.cliptripbe.infrastructure.inital;

import static com.cliptripbe.infrastructure.inital.type.DefaultData.ACCOMMODATION_SEOUL;
import static com.cliptripbe.infrastructure.inital.type.DefaultData.BUSAN_ACCESSIBLE_TOURISM;
import static com.cliptripbe.infrastructure.inital.type.DefaultData.INCHEON_ACCESSIBLE_TOURISM;
import static com.cliptripbe.infrastructure.inital.type.DefaultData.SOKCHO_OPEN_TOURISM;
import static com.cliptripbe.infrastructure.inital.type.DefaultData.STORAGE_SEOUL;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.infrastructure.inital.initaler.BookmarkInitializer;
import com.cliptripbe.infrastructure.inital.initaler.PlaceInitializer;
import com.cliptripbe.infrastructure.inital.type.DefaultData;
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

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

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