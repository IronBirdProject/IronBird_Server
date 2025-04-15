package com.trip.IronBird_Server.plan.adapter.mapper;

import com.trip.IronBird_Server.plan.adapter.dto.ScheduleDto;
import com.trip.IronBird_Server.plan.domain.Schedule;
import org.springframework.stereotype.Component;


@Component
public class ScheduleMapper {

    public ScheduleDto toDto(Schedule schedule){
        return ScheduleDto.builder()
                .id(schedule.getId())
                .day(schedule.getDay())
                .time(schedule.getTime())
                .description(schedule.getDescription())
                .cost(schedule.getCost())
                .memo(schedule.getMemo())
                .planId(schedule.getPlan() != null ? schedule.getPlan().getId() : null)
                .build();
    }




}
