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

    @Column(name = "youtubeVideoId", nullable = false)
    private String youtubeVideoId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summaryKo", nullable = false)
    private String summaryKo;

    @Column(name = "summaryTranslated")
    private String summaryTranslated;

    @Column(name = "translatedLangCode")
    private String translatedLangCode;
}
