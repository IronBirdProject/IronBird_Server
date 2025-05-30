package com.trip.IronBird_Server.plan.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanUpdateDto {
    private String title;
    private String startedDate;
    private String endDate;
}
