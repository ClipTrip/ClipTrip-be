package com.cliptripbe.feature.place.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceTranslationRepository extends JpaRepository<PlaceTranslation, Long> {

    Optional<PlaceTranslation> findByPlaceAndLanguage(Place place, Language language);

    @Query("SELECT pt FROM PlaceTranslation pt WHERE pt.place.id IN :placeIds AND pt.language = :language")
    List<PlaceTranslation> findByPlaceIdInAndLanguage(
        @Param("placeIds") List<Long> placeIds, @Param("language") Language language);
}
