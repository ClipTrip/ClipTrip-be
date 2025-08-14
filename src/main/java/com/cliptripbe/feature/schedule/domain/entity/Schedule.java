package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.user.domain.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    private static final String DESCRIPTION = "Trip's description";
    private static final String NAME = "Trip";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @JoinColumn
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("placeOrder ASC")
    private List<SchedulePlace> schedulePlaceList = new ArrayList<>();

    @Builder
    public Schedule(String name, User user, String description) {
        this.name = name;
        this.user = user;
        this.description = description;
    }

    public static Schedule createDefault(User user) {
        return Schedule.builder()
            .user(user)
            .description(DESCRIPTION)
            .name(NAME)
            .build();
    }

    public void addSchedulePlace(SchedulePlace schedulePlace) {
        this.schedulePlaceList.add(schedulePlace);
    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyDescription(String description) {
        this.description = description;
    }

    public void clearSchedulePlaceList() {
        this.schedulePlaceList.clear();
    }
}

