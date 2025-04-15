package com.trip.IronBird_Server.plan.adapter.dto;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long id;
    private Long userId;
    private String userName; //유저 닉네임(이름)
    private String destination;
    private String startedDate;
    private String endDate;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    // 스케쥴 dto
    private List<ScheduleDto> schedules;
}
