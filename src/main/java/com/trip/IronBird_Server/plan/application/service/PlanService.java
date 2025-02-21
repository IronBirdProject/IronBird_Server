package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;

import java.util.List;


public interface PlanService {
    public List<PlanDto> getAllPlans(); //모든 플랜 가져오기
    public List<PlanDto> getPlansByUserId(Long userId); //유저아이디별 플랜 가져오기
    public PlanDto createPlan(PlanDto planDto, Long userIdFromToken); // 플랜 생성
    public PlanDto updatePlan(Long id,PlanDto planDto); //플랜 수정
    public void deletePlan(Long id); // 플랜 삭제
}
