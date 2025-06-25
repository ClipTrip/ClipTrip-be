package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @JoinColumn
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchedulePlace> schedulePlaceList = new ArrayList<>();

    @Builder
    public Schedule(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void addSchedulePlace(SchedulePlace schedulePlace) {
        this.schedulePlaceList.add(schedulePlace);
    }

    public void updateName(String name) {

    }
}
