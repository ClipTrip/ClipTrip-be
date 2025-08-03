package com.cliptripbe.infrastructure.adapter.out.kakao;


import static com.cliptripbe.global.response.type.ErrorType.FAIL_KAKAO_MOBILITY;
import static com.cliptripbe.global.response.type.ErrorType.KAKAO_MOBILITY_NO_RESPONSE;

import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsRequest;
import com.cliptripbe.infrastructure.adapter.out.kakao.dto.WaypointsDirectionsResponse;
import com.cliptripbe.infrastructure.port.kakao.RouteSearchPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMobilityAdapter implements RouteSearchPort {

    @Qualifier("kakaoMobilityRestClient")
    private final RestClient kakaoMobilityRestClient;

    @Override
    public WaypointsDirectionsResponse getDirections(WaypointsDirectionsRequest request) {
        long start = System.currentTimeMillis();

        try {
            WaypointsDirectionsResponse response = kakaoMobilityRestClient.post()
                .uri("/v1/waypoints/directions")
                .body(request)
                .retrieve()
                .toEntity(WaypointsDirectionsResponse.class)
                .getBody();

            if (response == null) {
                throw new CustomException(KAKAO_MOBILITY_NO_RESPONSE);
            }

            long elapsed = System.currentTimeMillis() - start;
            log.info("카카오 모빌리티 길찾기 호출 레이턴시: {} ms", elapsed);

            return response;
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("카카오 모빌리티 API 호출 실패 (레이턴시: {} ms): {}", elapsed, e.getMessage(), e);
            throw new CustomException(FAIL_KAKAO_MOBILITY);
        }

    }
}

