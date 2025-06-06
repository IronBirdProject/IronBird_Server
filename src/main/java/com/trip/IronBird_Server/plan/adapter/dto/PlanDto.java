package com.trip.IronBird_Server.plan.adapter.dto;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.domain.Schedule;
import com.trip.IronBird_Server.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.userId = plan.getUser().getId();
        this.userName = plan.getUser().getName();
        this.destination = plan.getDestination();
        this.startedDate = plan.getStartedDate();
        this.endDate = plan.getEndDate();


        this.schedules = plan.getSchedules()
                .stream()
                .map(ScheduleDto::new)
                .collect(Collectors.toList());
    }
}
