package com.trip.IronBird_Server.plan.infrastructure;

import com.trip.IronBird_Server.plan.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
