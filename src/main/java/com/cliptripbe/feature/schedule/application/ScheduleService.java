package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.application.PlaceFinder;
import com.cliptripbe.feature.schedule.api.dto.request.CreateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final PlaceFinder placeFinder;
    private final ScheduleRepository scheduleRepository;

    public void create(User user, CreateScheduleRequestDto createRentalRequest) {
        Schedule schedule = Schedule
            .builder()
            .user(user)
            .description(createRentalRequest.description())
            .name(createRentalRequest.scheduleName())
            .build();

        for (PlaceInfoRequestDto placeInfoRequestDto : createRentalRequest.placeInfoRequestDtos()) {
            SchedulePlace schedulePlace = SchedulePlace
                .builder()
                .place(placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto))
                .schedule(schedule)
                .build();
            schedule.addSchedulePlace(schedulePlace);
        }
        scheduleRepository.save(schedule);
    }

    public void create(User user) {
        Schedule schedule = Schedule.createDefault(user);
        scheduleRepository.save(schedule);
    }

    public void updateSchedule(
        User user,
        Long scheduleId,
        UpdateScheduleRequestDto updateSchedule
    ) {
        Schedule schedule = getSchedule(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        schedule.update(updateSchedule.scheduleName(), updateSchedule.description());
        schedule.clear();
        for (PlaceInfoRequestDto placeInfoRequestDto : updateSchedule.placeInfoRequestDtos()) {
            SchedulePlace newPlace = SchedulePlace.builder()
                .place(placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto))
                .schedule(schedule)
                .build();
            schedule.addSchedulePlace(newPlace);
        }
    }

    @Transactional(readOnly = true)
    public List<ScheduleListResponseDto> getUserScheduleList(User user) {
        List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
        return scheduleList
            .stream()
            .map(SchedulePlaceMapper::mapScheduleListResponseDto)
            .toList();
    }

    public void deleteSchedule(User user, Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        scheduleRepository.delete(schedule);
    }


    public ScheduleInfoResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        return SchedulePlaceMapper.mapScheduleInfoResponseDto(
            schedule);
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));
    }
}