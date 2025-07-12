package com.cliptripbe.feature.schedule.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.application.PlaceTranslationFinder;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PlaceService placeService;
    private final PlaceTranslationFinder placeTranslationFinder;


//    public void create(User user, CreateScheduleRequestDto createRentalRequest) {
//        Schedule schedule = Schedule
//            .builder()
//            .user(user)
//            .description(createRentalRequest.description())
//            .name(createRentalRequest.scheduleName())
//            .build();
//
//        for (PlaceInfoRequestDto placeInfoRequestDto : createRentalRequest.placeInfoRequestDtos()) {
//            SchedulePlace schedulePlace = SchedulePlace
//                .builder()
//                .place(placeFinder.getPlaceByPlaceInfo(placeInfoRequestDto))
//                .schedule(schedule)
//                .build();
//            schedule.addSchedulePlace(schedulePlace);
//        }
//        scheduleRepository.save(schedule);
//    }

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

        schedule.modifyInfo(updateSchedule.scheduleName(), updateSchedule.description());
        schedule.clear();

        for (PlaceInfoRequestDto placeInfoRequestDto : updateSchedule.placeInfoRequestDtos()) {
            Place place = placeService.getPlaceByPlaceInfo(placeInfoRequestDto);
            SchedulePlace newPlace = SchedulePlace.builder()
                .place(place)
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


    public ScheduleInfoResponseDto getScheduleById(
        User user,
        Long scheduleId
    ) {
        Schedule schedule = getSchedule(scheduleId);
        if (user.getLanguage() == KOREAN) {
            return SchedulePlaceMapper.mapScheduleInfoResponseDto(
                schedule);
        }
        List<PlaceListResponseDto> placeListResponseDtos = new ArrayList<>();
        for (SchedulePlace sp : schedule.getSchedulePlaceList()) {
            Place place = sp.getPlace();
            PlaceTranslation placeTranslation = placeTranslationFinder.getByPlaceAndLanguage(
                place,
                user.getLanguage());
            placeListResponseDtos.add(PlaceListResponseDto.of(place, placeTranslation));
        }
        return SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule, placeListResponseDtos);
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));
    }
}