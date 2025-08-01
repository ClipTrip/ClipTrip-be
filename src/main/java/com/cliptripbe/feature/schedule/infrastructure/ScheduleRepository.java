package com.cliptripbe.feature.schedule.infrastructure;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUser(User user);

    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.schedulePlaceList sp WHERE s.id = :scheduleId")
    Optional<Schedule> findByIdWithSchedulePlaces(@Param("scheduleId") Long scheduleId);

    @EntityGraph(attributePaths = {
        "schedulePlaceList",
        "schedulePlaceList.place",
        "schedulePlaceList.place.placeTranslations"
    })
    @Query("SELECT s FROM Schedule s WHERE s.id = :scheduleId")
    Optional<Schedule> findByIdWithSchedulePlacesAndTranslations(
        @Param("scheduleId") Long scheduleId);
}
