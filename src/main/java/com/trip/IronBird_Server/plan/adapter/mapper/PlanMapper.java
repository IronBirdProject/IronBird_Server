package com.trip.IronBird_Server.plan.adapter.mapper;

import com.trip.IronBird_Server.plan.adapter.dto.PlanCreateDto;
import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.adapter.dto.PlanPreviewDto;
import com.trip.IronBird_Server.plan.domain.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlanMapper {

    private final ScheduleMapper scheduleMapper;
    public PlanDto toDto(Plan plan){
        return PlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .userName(plan.getUser().getName())
                .title(plan.getTitle())
                .destination(plan.getDestination())
                .startedDate(plan.getStartedDate())
                .endDate(plan.getEndDate())
                .createdTime(plan.getCreated_time())
                .modifiedTime(plan.getModified_time())
                .schedules( plan.getSchedules() != null
                        ? plan.getSchedules().stream()
                        .map(scheduleMapper::toDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>() )
                .build();
    }

    public PlanPreviewDto toDtoPreview(Plan plan){
        return PlanPreviewDto.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .title(plan.getTitle())
                .userName(plan.getUser().getName())
                .destination(plan.getDestination())
                .startedDate(plan.getStartedDate())
                .endDate(plan.getEndDate())
                .build();
    }

    public PlanCreateDto toCreateDto(Plan plan) {
        return PlanCreateDto.builder()
                .userId(plan.getUser().getId())
                .title(plan.getTitle())
                .destination(plan.getDestination())
                .startedDate(plan.getStartedDate())
                .endDate(plan.getEndDate())
                .build();
    }
}