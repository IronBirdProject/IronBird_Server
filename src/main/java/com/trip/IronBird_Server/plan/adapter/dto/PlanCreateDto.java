package com.trip.IronBird_Server.plan.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanCreateDto {
    private String title;
    private String destination;
    private String startedDate;
    private String endDate;
    private Long userId;
//    private String imageUrl;
}
