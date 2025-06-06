package com.trip.IronBird_Server.plan.adapter.dto;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {
    private Long id;
    private int day;
    private String time;
    private String description;
    private int cost;
    private String memo;
    private Long planId;


    public ScheduleDto(ScheduleDto scheduleDto) {
        this.id = scheduleDto.getId();
        this.day = scheduleDto.getDay();
        this.time = scheduleDto.getDescription();
        this.cost = scheduleDto.getCost();
        this.memo = scheduleDto.getMemo();
        this.planId = scheduleDto.getPlanId();
    }
}
