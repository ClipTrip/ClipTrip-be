package com.cliptripbe.feature.schedule.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;

import com.cliptripbe.feature.place.api.dto.PlaceInfoRequestDto;
import com.cliptripbe.feature.place.api.dto.response.PlaceListResponseDto;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.schedule.api.dto.request.UpdateScheduleRequestDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleInfoResponseDto;
import com.cliptripbe.feature.schedule.api.dto.response.ScheduleListResponseDto;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.domain.impl.ScheduleFinder;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PlaceService placeService;
    private final ScheduleFinder scheduleFinder;

    @Transactional
    public void create(User user) {
        Schedule schedule = Schedule.createDefault(user);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void updateSchedule(
        User user,
        Long scheduleId,
        UpdateScheduleRequestDto updateSchedule
    ) {
        Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        schedule.modifyInfo(updateSchedule.scheduleName(), updateSchedule.description());
        schedule.clear();

        Integer placeOrder = 0;
        for (PlaceInfoRequestDto placeInfoRequestDto : updateSchedule.placeInfoRequestDtos()) {
            Place place = placeService.findOrCreatePlaceByPlaceInfo(placeInfoRequestDto);
            SchedulePlace newPlace = SchedulePlace.builder()
                .place(place)
                .schedule(schedule)
                .placeOrder(placeOrder)
                .build();
            schedule.addSchedulePlace(newPlace);
            placeOrder++;
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

    @Transactional
    public void deleteSchedule(User user, Long scheduleId) {
        Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleInfoResponseDto getScheduleById(
        User user,
        Long scheduleId
    ) {
        if (user.getLanguage() == KOREAN) {
            Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);
            return SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule);
        }
        Schedule schedule = scheduleFinder.getByIdWithSchedulePlacesAndTranslations(scheduleId);
        
        List<PlaceListResponseDto> placeListResponseDtos = schedule.getSchedulePlaceList().stream()
            .map(sp -> {
                Place place = sp.getPlace();
                Integer placeOrder = sp.getPlaceOrder();
                PlaceTranslation translation = place.getTranslationByLanguage(user.getLanguage());
                return PlaceListResponseDto.of(place, translation, placeOrder);
            })
            .toList();

        return SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule, placeListResponseDtos);
    }


}