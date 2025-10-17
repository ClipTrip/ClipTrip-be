package com.cliptripbe.feature.video.application;

import static com.cliptripbe.global.response.type.ErrorType.FAIL_EXTRACT_PLACE;
import static com.cliptripbe.global.response.type.ErrorType.FAIL_GENERATE_SUMMARY;

import com.cliptripbe.feature.place.dto.PlaceDto;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.util.ChatGPTUtils;
import com.cliptripbe.infrastructure.port.kakao.PlaceSearchPort;
import com.cliptripbe.infrastructure.port.openai.AiTextProcessorPort;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoAsyncProcessor {

    private final AiTextProcessorPort aiTextProcessorPort;
    private final PlaceSearchPort placeSearchPort;

    @Async("videoExtractExecutor")
    public CompletableFuture<List<PlaceDto>> extractAndSearchPlaces(String requestPlacePrompt) {
        long start = System.currentTimeMillis();

        try {
            long gptStart = System.currentTimeMillis();
            log.info("장소 추출 시작 - Thread: {}", Thread.currentThread().getName());

            String gptPlaceResponse = aiTextProcessorPort.askPlaceExtraction(requestPlacePrompt);
            List<String> extractPlacesText = ChatGPTUtils.extractPlaces(gptPlaceResponse);
            long gptElapsed = System.currentTimeMillis() - gptStart;

            long kakaoStart = System.currentTimeMillis();
            return placeSearchPort.searchFirstPlacesInParallelAsync(extractPlacesText)
                .whenComplete((result, ex) -> {
                    long kakaoElapsed = System.currentTimeMillis() - kakaoStart;
                    long totalElapsed = System.currentTimeMillis() - start;

                    if (ex != null) {
                        log.error("장소 검색 실패 - 소요시간: {} ms", kakaoElapsed, ex);
                    } else {
                        log.info("장소(gpt + Kakao) 전체 레이턴시: {} ms (GPT: {}ms, Kakao: {}ms)",
                            totalElapsed, gptElapsed, kakaoElapsed);
                    }
                });

        } catch (Exception e) {
            log.error("장소 추출 실패 - Thread: {}", Thread.currentThread().getName(), e);
            throw new CustomException(FAIL_EXTRACT_PLACE);
        }
    }

    @Async("videoExtractExecutor")
    public CompletableFuture<String> generateVideoSummary(String requestSummaryPrompt) {
        try {
            long start = System.currentTimeMillis();
            log.info("요약 생성 시작 - Thread: {}", Thread.currentThread().getName());

            String gptSummaryResponse = aiTextProcessorPort.ask(requestSummaryPrompt);
            String summaryKo = ChatGPTUtils.removeLiteralNewlines(gptSummaryResponse);

            log.info("요약 생성 완료 - Thread: {}", Thread.currentThread().getName());
            log.info("요약 생성 호출 레이턴시: {} ms", System.currentTimeMillis() - start);

            return CompletableFuture.completedFuture(summaryKo);
        } catch (Exception e) {
            log.error("요약 생성 실패 - Thread: {}", Thread.currentThread().getName(), e);
            throw new CustomException(FAIL_GENERATE_SUMMARY);
        }
    }
}
