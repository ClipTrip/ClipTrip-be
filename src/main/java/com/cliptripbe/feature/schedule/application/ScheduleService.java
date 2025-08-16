package com.cliptripbe.feature.schedule.application;

import static com.cliptripbe.feature.schedule.application.ScheduleResponseAssembler.createBookmarkResponseForForeign;
import static com.cliptripbe.feature.schedule.application.ScheduleResponseAssembler.createScheduleListResponse;
import static com.cliptripbe.feature.schedule.application.ScheduleResponseAssembler.createScheduleResponseForKorean;

import com.cliptripbe.feature.bookmark.domain.service.BookmarkFinder;
import com.cliptripbe.feature.place.application.PlaceService;
import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.dto.request.PlaceInfoRequest;
import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.domain.entity.SchedulePlace;
import com.cliptripbe.feature.schedule.domain.impl.ScheduleFinder;
import com.cliptripbe.feature.schedule.dto.request.PlaceWithOrderRequest;
import com.cliptripbe.feature.schedule.dto.request.UpdateScheduleRequest;
import com.cliptripbe.feature.schedule.dto.response.ScheduleListResponse;
import com.cliptripbe.feature.schedule.dto.response.ScheduleResponse;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.translate.dto.response.TranslationInfoDto;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.type.Language;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import java.util.List;
import java.util.Map;
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
    private final BookmarkFinder bookmarkFinder;

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
        return createScheduleListResponse(scheduleList);
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
        Schedule scheduleWithPlaces = scheduleFinder.getScheduleWithSchedulePlaces(scheduleId);
        scheduleWithPlaces.validAccess(user);
        List<Place> places = scheduleWithPlaces.getPlaces();
        List<Long> placeIdList = places.stream().map(Place::getId).toList();
        Map<Long, List<Long>> bookmarkIdsMap = bookmarkFinder.findBookmarkIdsByPlaceIds(
            user.getId(), placeIdList);

        if (user.getLanguage() == Language.KOREAN) {
            return createScheduleResponseForKorean(
                scheduleWithPlaces,
                bookmarkIdsMap,
                user
            );
        }
        Map<Long, TranslationInfoDto> translationsForPlaces = placeService.getTranslationsForPlaces(places,
            user.getLanguage());

        return createBookmarkResponseForForeign(scheduleWithPlaces, translationsForPlaces, bookmarkIdsMap, user);
    }


    public ScheduleResponse createScheduleByVideo(User user, List<Place> placeList) {
        List<Long> placeIdList = placeList.stream().map(Place::getId).toList();
        Map<Long, List<Long>> bookmarkIdsMap = bookmarkFinder.findBookmarkIdsByPlaceIds(
            user.getId(), placeIdList);
        Schedule schedule = scheduleRegister.createDefaultSchedule(user);
        IntStream.range(0, placeList.size())
            .mapToObj(i -> SchedulePlace.builder()
                .place(placeList.get(i))
                .schedule(schedule)
                .placeOrder(i)
                .build()
            )
            .forEach(schedule::addSchedulePlace);
        if (user.getLanguage() == Language.KOREAN) {
            return createScheduleResponseForKorean(
                schedule,
                bookmarkIdsMap,
                user
            );
        }
        Map<Long, TranslationInfoDto> translationsForPlaces = placeService.getTranslationsForPlaces(placeList,
            user.getLanguage());
        return createBookmarkResponseForForeign(schedule, translationsForPlaces, bookmarkIdsMap, user);
    }
}