package com.cliptripbe.feature.place.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.type.PlaceType;
import java.util.List;
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

    @Query("SELECT p FROM Place p WHERE p.placeType = :placeType AND p.address.roadAddress LIKE %:region%")
    List<Place> findByRegionAndPlaceType(@Param("region") String region,
        @Param("placeType") PlaceType placeType);

    List<Place> findByPlaceType(PlaceType placeType);

    @Query("""
        SELECT p
          FROM Place p
         WHERE p.address.roadAddress IN :addressList
           AND p.name                IN :placeNameList
        """)
    List<Place> findExistingPlaceByAddressAndName(
        @Param("addressList") List<String> addressList,
        @Param("placeNameList") List<String> placeNameList
    );

    @Query("SELECT p FROM Place p WHERE p.address.roadAddress IN :addressList")
    List<Place> findExistingPlaceByAddress(@Param("addressList") List<String> addressList);

    Optional<Place> findByNameAndAddressRoadAddress(String name, String roadAddress);

}
