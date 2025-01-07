package com.trip.IronBird_Server.plan.service;

import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    // 모든 플랜 조회
    public List<PlanDto> getAllPlans() {
        return planRepository.findAll().stream()
                .map(plan -> PlanDto.builder()
                        .id(plan.getId())
                        .userId(plan.getUser().getId())
                        .startedTime(plan.getStartedTime())
                        .endTime(plan.getEndTime())
                        .createdTime(plan.getCreated_time())
                        .modifiedTime(plan.getModified_time())
                        .build())
                .collect(Collectors.toList());
    }

    // 특정 유저의 플랜 조회
    public List<PlanDto> getPlansByUserId(Long userId){
        return planRepository.findByUserId(userId).stream()
                .map(plan -> PlanDto.builder()
                        .id(plan.getId())
                        .userId(plan.getUser().getId())
                        .startedTime(plan.getStartedTime())
                        .endTime(plan.getEndTime())
                        .createdTime(plan.getCreated_time())
                        .modifiedTime(plan.getModified_time())
                        .build())
                .collect(Collectors.toList());
    }
}
