package com.cliptripbe.feature.schedule.domain.impl;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
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

    public Schedule getScheduleWithSchedulePlaces(Long scheduleId) {
        return scheduleRepository.findByIdWithSchedulePlaces(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    public Schedule getByIdWithSchedulePlacesAndTranslations(Long scheduleId) {
        return scheduleRepository.findByIdWithSchedulePlacesAndTranslations(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
