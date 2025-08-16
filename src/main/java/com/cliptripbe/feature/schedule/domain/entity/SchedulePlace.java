package com.cliptripbe.feature.schedule.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchedulePlace that = (SchedulePlace) o;

        // 두 객체가 모두 아직 DB에 저장되지 않은 상태(id가 null)일 때
        // 비즈니스 키(schedule, place, placeOrder)로 동일성을 판단합니다.
        if (this.id == null && that.id == null) {
            return Objects.equals(this.schedule, that.schedule) &&
                Objects.equals(this.place, that.place) &&
                Objects.equals(this.placeOrder, that.placeOrder);
        }

        // 두 객체 중 하나라도 DB에 저장되어 id가 있다면,
        // 오직 id만으로 동일성을 판단합니다.
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        } else {
            return Objects.hash(schedule, place, placeOrder);
        }
    }
}

