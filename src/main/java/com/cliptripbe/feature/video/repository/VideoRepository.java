package com.cliptripbe.feature.video.repository;

import com.cliptripbe.feature.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
