package com.cliptripbe.feature.schedule.infrastructure;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUser(User user);
}
