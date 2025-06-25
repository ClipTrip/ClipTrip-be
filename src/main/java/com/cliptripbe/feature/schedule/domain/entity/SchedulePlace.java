package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.place.domain.vo.PlaceDetailVO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SchedulePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Schedule schedule;

    @ManyToOne
    Place place;

    @Builder
    public SchedulePlace(Schedule schedule, Place place) {
        this.schedule = schedule;
        this.place = place;
    }

    public PlaceDetailVO toVO() {
        return new PlaceDetailVO(place.getId(), place.getName(), place.getAccessibilityFeatures());
    }
}
