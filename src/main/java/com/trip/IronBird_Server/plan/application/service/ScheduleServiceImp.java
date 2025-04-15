package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.dto.ScheduleDto;
import com.trip.IronBird_Server.plan.adapter.mapper.ScheduleMapper;
import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.domain.Schedule;
import com.trip.IronBird_Server.plan.infrastructure.PlanRepository;
import com.trip.IronBird_Server.plan.infrastructure.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImp implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PlanRepository planRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleDto createSchedule(ScheduleDto scheduleDto, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + planId));

        Schedule schedule = Schedule.builder()
                .day(scheduleDto.getDay())
                .cost(scheduleDto.getCost())
                .description(scheduleDto.getDescription())
                .time(scheduleDto.getTime())
                .memo(scheduleDto.getMemo())
                .plan(plan)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.schedulMapper(savedSchedule);
    }
}
