package com.trip.IronBird_Server.plan.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleCreateDto {
    private int day;
    private String time;
    private String description;
    private Integer cost;
    private String memo;
    private Long planId;
}
