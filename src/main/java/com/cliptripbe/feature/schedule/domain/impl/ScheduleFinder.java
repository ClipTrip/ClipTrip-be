package com.cliptripbe.feature.schedule.domain.impl;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleFinder {

    private final ScheduleRepository scheduleRepository;

    public Schedule getScheduleWithSchedulePlaces(Long scheduleId) {
        return scheduleRepository.findByIdWithSchedulePlaces(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    public Optional<Schedule> findByIdWithSchedulePlacesAndTranslations(
        Long scheduleId,
        String language
    ) {
        return scheduleRepository.findByIdWithSchedulePlacesAndTranslations(scheduleId, language);
    }
}
