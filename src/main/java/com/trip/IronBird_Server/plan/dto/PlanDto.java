package com.trip.IronBird_Server.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long id;
    private Long userId;
    private String startedTime;
    private String endTime;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

}
