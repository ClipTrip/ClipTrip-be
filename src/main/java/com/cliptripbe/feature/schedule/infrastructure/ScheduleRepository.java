package com.cliptripbe.feature.schedule.infrastructure;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.domain.type.Language;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUser(User user);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.schedulePlaceList sp WHERE s.id = :scheduleId")
    Optional<Schedule> findByIdWithSchedulePlaces(@Param("scheduleId") Long scheduleId);

    /**
         * Retrieves a {@link Schedule} by its ID, including its associated schedule places, each place, and the place translations filtered by the specified language.
         *
         * @param scheduleId the ID of the schedule to retrieve
         * @param language the language used to filter place translations
         * @return an {@link Optional} containing the matching {@link Schedule} with its places and filtered translations, or empty if not found
         */
        @Query("SELECT s FROM Schedule s " +
        "JOIN FETCH s.schedulePlaceList sp " +
        "JOIN FETCH sp.place p " +
        "LEFT JOIN FETCH p.placeTranslations pt " +
        "WHERE s.id = :scheduleId AND pt.language = :language")
    Optional<Schedule> findByIdWithSchedulePlacesAndTranslations(
        @Param("scheduleId") Long scheduleId,
        @Param("language") Language language);
}
