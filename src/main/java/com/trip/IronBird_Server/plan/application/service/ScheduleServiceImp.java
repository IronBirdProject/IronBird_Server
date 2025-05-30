package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.dto.ScheduleCreateDto;
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
    public ScheduleDto createSchedule(ScheduleCreateDto scheduleCreateDto, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + planId));

        Schedule schedule = Schedule.builder()
                .day(scheduleCreateDto.getDay())
                .cost(scheduleCreateDto.getCost())
                .description(scheduleCreateDto.getDescription())
                .time(scheduleCreateDto.getTime())
                .memo(scheduleCreateDto.getMemo())
                .plan(plan)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(savedSchedule);
    }
}
