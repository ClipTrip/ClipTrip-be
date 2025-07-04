package com.cliptripbe.feature.schedule.application;

import com.cliptripbe.feature.schedule.domain.entity.Schedule;
import com.cliptripbe.feature.schedule.infrastructure.ScheduleRepository;
import com.cliptripbe.feature.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleRegister {

    private final ScheduleRepository scheduleRepository;

    public Schedule registerSchedule(User user) {
        Schedule schedule = Schedule.createDefault(user);
        return scheduleRepository.save(schedule);
    }

}
