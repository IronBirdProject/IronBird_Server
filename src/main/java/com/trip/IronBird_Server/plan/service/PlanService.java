package com.trip.IronBird_Server.plan.service;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.repository.PlanRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

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

    //플랜 생성 서비스
    @Transactional
    public PlanDto createPlan(PlanDto planDto){
        User user = userRepository.findById(planDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + planDto.getUserId()));

        Plan plan = Plan.builder()
                .startedTime(planDto.getStartedTime())
                .endTime(planDto.getEndTime())
                .user(user) //user 설정
                .build();

        //User 는 외부에서 가져오기
        Plan savedPlan = planRepository.save(plan);
        return PlanDto.builder()
                .id(savedPlan.getId())
                .userId(savedPlan.getUser().getId())
                .startedTime(savedPlan.getStartedTime())
                .endTime(savedPlan.getEndTime())
                .createdTime(savedPlan.getCreated_time())
                .modifiedTime(savedPlan.getModified_time())
                .build();
    }



}
