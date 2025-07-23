package com.cliptripbe.feature.video.domain.service;

import com.cliptripbe.feature.video.domain.entity.Video;
import com.cliptripbe.feature.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoRegister {

    private final VideoRepository videoRepository;

    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }
}
