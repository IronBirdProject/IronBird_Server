package com.trip.IronBird_Server.plan.adapter.mapper;

import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.domain.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {
    public PlanDto toDto(Plan plan){
        return PlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .startedDate(plan.getStartedDate())
                .endDate(plan.getEndDate())
                .createdTime(plan.getCreated_time())
                .modifiedTime(plan.getModified_time())
                .build();
    }
}
