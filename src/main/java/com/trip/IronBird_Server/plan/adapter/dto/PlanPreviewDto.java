package com.trip.IronBird_Server.plan.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 예시: PlanPreviewDto (리스트 전용)
public class PlanPreviewDto {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String destination;
    private String startedDate;
    private String endDate;
    private String imageUrl;
}
