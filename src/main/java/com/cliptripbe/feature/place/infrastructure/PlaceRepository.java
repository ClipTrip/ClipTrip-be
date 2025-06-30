package com.cliptripbe.feature.place.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p WHERE p.name = :name AND p.address.roadAddress = :roadAddress")
    Optional<Place> findPlaceByPlaceInfo(
        @Param("name") String name,
        @Param("roadAddress") String roadAddress
    );
}
