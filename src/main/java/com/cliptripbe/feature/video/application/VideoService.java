package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.video.domain.service.VideoRegister;
import com.cliptripbe.feature.video.domain.entity.Video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

//    private final ScheduleRegister scheduleRegister;
//    private final ScheduleFinder scheduleFinder;
//    private final PlaceRegister placeRegister;
//    private final VideoRepository videoRepository;
//    private final PlaceTranslationService placeTranslationService;
//
//    private final ChatGptAdapter chatGptAdapter;
//    private final KakaoMapService kakaoMapService;
//    private final CaptionAdapter captionAdapter;


    private final VideoRegister videoRegister;

//    public VideoScheduleResponse extractPlace(User user, ExtractPlaceRequest request) {
//        CaptionUtils.extractVideoId(request.youtubeUrl());
//        CaptionResponse caption = captionService.getCaptions(
//            CaptionRequest.of(request.youtubeUrl())
//        );
//
//        String requestPlacePrompt =
//            PromptConstants.EXTRACT_PLACE + System.lineSeparator() + caption.captions();
//
//        String requestSummaryPrompt =
//            PromptConstants.SUMMARY_CAPTION + System.lineSeparator() + caption.captions();
//
//        String requestSummaryEnPrompt =
//            PromptConstants.SUMMARY_CAPTION_EN + System.lineSeparator() + caption.captions();
//
//        List<String> extractPlacesText = chatGPTService.askPlaceExtraction(requestPlacePrompt)
//            .subscribeOn(Schedulers.boundedElastic())
//            .map(ChatGPTUtils::extractPlaces)
//            .blockOptional()
//            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));
//
//        String summaryKo = chatGPTService.ask(requestSummaryPrompt)
//            .subscribeOn(Schedulers.boundedElastic())
//            .map(ChatGPTUtils::removeLiteralNewlines)
//            .blockOptional()
//            .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));
//
//        String summaryTranslated = null;
//        if (user.getLanguage() == Language.ENGLISH) {
//            summaryTranslated = chatGPTService.ask(requestSummaryEnPrompt)
//                .subscribeOn(Schedulers.boundedElastic())
//                .map(ChatGPTUtils::removeLiteralNewlines)
//                .blockOptional()
//                .orElseThrow(() -> new CustomException(CHATGPT_NO_RESPONSE));
//        }
//
//        List<PlaceDto> places = kakaoMapService.searchFirstPlaces(extractPlacesText)
//            .subscribeOn(Schedulers.boundedElastic())
//            .blockOptional()
//            .orElseThrow(() -> new CustomException(KAKAO_MAP_NO_RESPONSE));
//
//        List<Place> placeEntities = placeRegister.registerAllPlaces(places);
//        if (user.getLanguage() == Language.ENGLISH) {
//            placeEntities.forEach(placeTranslationService::registerPlace);
//        }
//        Video video = videoRepository.save(request.toVideo(summaryKo, summaryTranslated));
//        Schedule schedule = scheduleRegister.createDefaultSchedule(user);
//
//        // 스케줄 서비스로 뺴기
//        IntStream.range(0, placeEntities.size())
//            .mapToObj(i -> SchedulePlace.builder()
//                .place(placeEntities.get(i))
//                .schedule(schedule)
//                .placeOrder(i)
//                .build()
//            )
//            .forEach(schedule::addSchedulePlace);
//
//        Schedule scheduleEntity = scheduleFinder.getByIdWithSchedulePlacesAndTranslations(
//            schedule.getId());
////
//        List<PlaceListResponseDto> placeListResponseDtos = scheduleEntity.getSchedulePlaceList()
//            .stream()
//            .map(sp -> {
//                Place place = sp.getPlace();
//                Integer placeOrder = sp.getPlaceOrder();
//                PlaceTranslation translation = place.getTranslationByLanguage(user.getLanguage());
//                return PlaceListResponseDto.of(place, translation, placeOrder);
//            })
//            .toList();
//
//        return VideoScheduleResponse.of(video, scheduleEntity, placeListResponseDtos);
//    }

    public Video createVideo(Video video) {
        return videoRegister.createVideo(video);
    }
}