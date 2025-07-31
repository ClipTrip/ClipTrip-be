package com.cliptripbe.feature.video.application;

import com.cliptripbe.feature.video.domain.service.VideoRegister;
import com.cliptripbe.feature.video.domain.entity.Video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRegister videoRegister;

    public Video createVideo(Video video) {
        return videoRegister.createVideo(video);
    }
}