package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final Set<SchedulePlace> schedulePlaces = new LinkedHashSet<>();

    public List<Place> getPlaces() {
        return schedulePlaces.stream()
            .sorted(Comparator.comparingInt(SchedulePlace::getPlaceOrder))
            .map(SchedulePlace::getPlace)
            .collect(Collectors.toList());
    }

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

    public void addSchedulePlace(SchedulePlace newSchedulePlace) {
//        Long newPlaceId = newSchedulePlace.getPlace().getId();
//        boolean exists = schedulePlaces.stream()
//            .anyMatch(sp -> sp.getId().equals(newPlaceId));
//        if (exists) {
//            throw new CustomException(EXISTS_PLACE);
//        }
        this.schedulePlaces.add(newSchedulePlace);
    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyDescription(String description) {
        this.description = description;
    }

    public void clearSchedulePlaceList() {
        this.schedulePlaces.clear();
    }

    public void validAccess(User user) {
        if (!this.user.getId().equals(user.getId())) {
            throw new CustomException(ErrorType.ACCESS_DENIED_EXCEPTION);
        }
    }
}

