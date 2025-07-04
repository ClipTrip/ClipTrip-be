package com.cliptripbe.feature.video.domain;

import com.cliptripbe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "videos")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "youtube_video_id", nullable = false)
    private String youtubeVideoId;

    @Column(name = "summary_ko", columnDefinition = "TEXT", nullable = false)
    private String summaryKo;

    @Column(name = "summary_translated", columnDefinition = "TEXT")
    private String summaryTranslated;

    @Column(name = "translated_lang_code")
    private String translatedLangCode;
}
