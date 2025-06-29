package com.cliptripbe.feature.video.infrastructure;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
