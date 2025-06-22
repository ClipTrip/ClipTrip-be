package com.cliptripbe.feature.place.domain;

import lombok.Getter;

@Getter
public enum AccessibilityFeature {
    FREE_PARKING("무료주차 가능"),
    PAID_PARKING("유료주차 가능"),
    ENTRANCE_FOR_DISABLED("장애인용 출입문"),
    WHEELCHAIR_RENTAL("휠체어 대여"),
    DISABLED_TOILET("장애인 화장실"),
    EXCLUSIVE_PARKING("장애인 전용 주차장"),
    LARGE_PARKING("대형 주차장"),
    GUIDE_DOG_ALLOWED("시각장애인 안내견 동반 가능"),
    BRAILLE_GUIDE("점자 가이드 제공"),
    AUDIO_GUIDE_KR("오디오 가이드(한국어)");

    private final String description;

    AccessibilityFeature(String description) {
        this.description = description;
    }
}
