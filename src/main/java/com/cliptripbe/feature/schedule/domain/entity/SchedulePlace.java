package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import jakarta.persistence.Column;
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
    private Schedule schedule;

    @ManyToOne
    private Place place;

    @Column
    private Integer placeOrder;

    @Builder
    public SchedulePlace(Schedule schedule, Place place, Integer placeOrder) {
        this.schedule = schedule;
        this.place = place;
        this.placeOrder = placeOrder;
    }
}
