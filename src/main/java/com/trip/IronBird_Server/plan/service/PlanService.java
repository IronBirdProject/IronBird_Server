package com.trip.IronBird_Server.plan.service;

import com.trip.IronBird_Server.common.exception.UnauthorizedException;
import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.repository.PlanRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public PlanDto createPlan(PlanDto planDto, Long userIdFromToken){
        User user = userRepository.findById(planDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userIdFromToken));


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

    //플랜 수정 서비스
    @Transactional
    public PlanDto updatePlan(Long id,PlanDto planDto, Long userIdFromToken){

        //기존 프랜 조회
        Plan exitPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan Not found with ID : " + id));

        if(!exitPlan.getUser().getId().equals(userIdFromToken)){
            throw new UnauthorizedException("User is not authorized to update this plan.");
        }

        //변경할 데이터 가져와서 엔티티에 등록
        exitPlan.setStartedTime(planDto.getStartedTime());
        exitPlan.setEndTime(planDto.getEndTime());

        //수정된 데이터 저장
        Plan updatePlan = planRepository.save(exitPlan);

        // 저장된 결과를 PlanDto로 변환하여 반환
        return PlanDto.builder()
                .id(updatePlan.getId())
                .userId(updatePlan.getUser().getId())
                .startedTime(updatePlan.getStartedTime())
                .endTime(updatePlan.getEndTime())
                .createdTime(updatePlan.getCreated_time())
                .modifiedTime(updatePlan.getModified_time())
                .build();
    }

    //플랜 삭제 서비스
    public void deletePlan(Long id){
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        planRepository.delete(plan);
    }


}
