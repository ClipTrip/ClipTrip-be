package com.cliptripbe.feature.schedule.application;

import static com.cliptripbe.feature.user.domain.type.Language.KOREAN;

import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.entity.PlaceTranslation;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.domain.impl.ScheduleFinder;
import com.cliptripbe.feature.schedule.dto.request.PlaceWithOrderRequest;
import com.cliptripbe.feature.schedule.dto.request.UpdateScheduleRequest;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRegister scheduleRegister;
    private final ScheduleRepository scheduleRepository;

    private final PlaceService placeService;
    private final ScheduleFinder scheduleFinder;

    public void create(User user) {
        scheduleRegister.createDefaultSchedule(user);
    }

    @Transactional
    public void updateSchedule(
        User user,
        Long scheduleId,
        UpdateScheduleRequest request
    ) {
        Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }

        if (request.scheduleName() != null) {
            schedule.modifyName(request.scheduleName());
        }

        if (request.description() != null) {
            schedule.modifyDescription(request.description());
        }

        if (request.placeInfoRequests() != null && !request.placeInfoRequests().isEmpty()) {
            schedule.clearSchedulePlaceList();

            List<PlaceInfoRequest> placeInfoRequests = request.placeInfoRequests().stream()
                .map(PlaceWithOrderRequest::placeInfo)
                .toList();

            List<Place> places = placeService.findOrCreatePlacesByPlaceInfos(placeInfoRequests);

            for (int i = 0; i < request.placeInfoRequests().size(); i++) {
                PlaceWithOrderRequest placeWithOrder = request.placeInfoRequests().get(i);
                Place place = places.get(i);

                SchedulePlace newPlace = SchedulePlace.builder()
                    .place(place)
                    .schedule(schedule)
                    .placeOrder(placeWithOrder.placeOrder())
                    .build();
                schedule.addSchedulePlace(newPlace);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> getUserScheduleList(User user) {
        List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
        return scheduleList
            .stream()
            .map(SchedulePlaceMapper::mapScheduleListResponseDto)
            .toList();
    }

    public void deleteSchedule(User user, Long scheduleId) {
        Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleById(
        User user,
        Long scheduleId
    ) {
        if (user.getLanguage() == KOREAN) {
            Schedule schedule = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);
            // [리팩토링] 지금 추상화 안하고 분기로 받지만 of 팩토리 메소드 안에서 분기 확인
            return ScheduleResponse.of(schedule, user.getLanguage());
        }
        Schedule schedule = scheduleFinder.getByIdWithSchedulePlacesAndTranslations(scheduleId);

        List<PlaceListResponse> placeListResponses = schedule.getSchedulePlaceList().stream()
            .map(sp -> {
                Place place = sp.getPlace();
                Integer placeOrder = sp.getPlaceOrder();
                PlaceTranslation translation = place.getTranslationByLanguage(user.getLanguage());
                return PlaceListResponse.ofTranslation(place, translation, placeOrder);
            })
            .toList();

        return SchedulePlaceMapper.mapScheduleInfoResponseDto(schedule, placeListResponses);
    }


    public Schedule createScheduleByVideo(User user, List<Place> placeList) {
        Schedule schedule = scheduleRegister.createDefaultSchedule(user);
        IntStream.range(0, placeList.size())
            .mapToObj(i -> SchedulePlace.builder()
                .place(placeList.get(i))
                .schedule(schedule)
                .placeOrder(i)
                .build()
            )
            .forEach(schedule::addSchedulePlace);
        return schedule;
    }
}