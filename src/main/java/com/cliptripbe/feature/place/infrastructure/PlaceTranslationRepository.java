package com.cliptripbe.feature.place.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTranslationRepository extends JpaRepository<PlaceTranslation, Long> {

    Optional<PlaceTranslation> findByPlaceAndLanguage(Place place, Language language);
}
