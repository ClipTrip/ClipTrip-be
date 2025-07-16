package com.cliptripbe.feature.schedule.domain.impl;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduleFinder {

    private final ScheduleRepository scheduleRepository;

    /**
     * Retrieves a Schedule by its ID along with its associated schedule places.
     *
     * @param scheduleId the ID of the schedule to retrieve
     * @return the Schedule entity with its schedule places
     * @throws CustomException if the schedule is not found
     */
    public Schedule getScheduleWithSchedulePlaces(Long scheduleId) {
        return scheduleRepository.findByIdWithSchedulePlaces(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    /**
     * Retrieves a Schedule by its ID, including its associated schedule places and translations for the specified language.
     *
     * @param scheduleId the ID of the schedule to retrieve
     * @param language the language to filter schedule place translations
     * @return the Schedule with its schedule places and translations in the specified language
     * @throws CustomException if the schedule is not found
     */
    public Schedule getByIdWithSchedulePlacesAndTranslations(
        Long scheduleId,
        Language language
    ) {
        return scheduleRepository.findByIdWithSchedulePlacesAndTranslations(scheduleId, language)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    /**
     * Retrieves a Schedule by its ID.
     *
     * @param scheduleId the ID of the schedule to retrieve
     * @return the Schedule with the specified ID
     * @throws CustomException if no schedule with the given ID is found
     */
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
