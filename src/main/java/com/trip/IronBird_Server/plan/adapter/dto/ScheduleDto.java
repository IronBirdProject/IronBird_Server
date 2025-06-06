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

    public ScheduleDto(Schedule schedule){
        this.id = schedule.getId();
        this.day = schedule.getDay();
        this.time = schedule.getDescription();
        this.cost = schedule.getCost();
        this.memo = schedule.getMemo();
        this.planId = schedule.getPlan().getId();
    }
}
