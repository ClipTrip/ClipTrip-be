package com.cliptripbe.feature.place.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String placeName);
}
