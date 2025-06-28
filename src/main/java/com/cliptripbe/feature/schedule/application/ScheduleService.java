package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.place.application.PlaceFinder;
import com.cliptripbe.feature.place.domain.vo.PlaceVO;
import com.cliptripbe.feature.schedule.api.dto.request.CreateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
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

    final PlaceFinder placeFinder;
    final ScheduleRepository scheduleRepository;

    public void create(User user, CreateScheduleRequestDto createRentalRequest) {
        Schedule schedule = Schedule
            .builder()
            .user(user)
            .description(createRentalRequest.description())
            .name(createRentalRequest.scheduleName())
            .build();

        for (PlaceVO placeVo : createRentalRequest.placeVOList()) {
            SchedulePlace schedulePlace = SchedulePlace
                .builder()
                .place(placeFinder.getPlaceByName(placeVo.placeName()))
                .schedule(schedule)
                .build();
            schedule.addSchedulePlace(schedulePlace);
        }
        scheduleRepository.save(schedule);
    }

    public void updateSchedule(
        User user,
        Long scheduleId,
        UpdateScheduleRequestDto updateSchedule
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        schedule.updateName(updateSchedule.scheduleName());
        schedule.getSchedulePlaceList().clear();

        for (PlaceVO placeVo : updateSchedule.placeVOList()) {
            SchedulePlace newPlace = SchedulePlace.builder()
                .place(placeFinder.getPlaceByName(placeVo.placeName()))
                .schedule(schedule)
                .build();
            schedule.addSchedulePlace(newPlace);
        }
    }

    public void deleteSchedulePlace(Long scheduleId, Long placeIdList, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        schedule.getSchedulePlaceList().removeIf(sp ->
            sp.getPlace().getId().equals(placeIdList)
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleListResponseDto> getUserSchedule(User user) {
        List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
        return scheduleList
            .stream()
            .map(SchedulePlaceMapper::mapScheduleListResponseDto)
            .toList();
    }

    public void addPlaceInSchedule(User user, Long scheduleId, String placeName) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        SchedulePlace newPlace = SchedulePlace.builder()
            .place(placeFinder.getPlaceByName(placeName))
            .schedule(schedule)
            .build();
        schedule.addSchedulePlace(newPlace);
        scheduleRepository.save(schedule);
    }
}