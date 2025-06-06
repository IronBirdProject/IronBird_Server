package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.dto.PlanCreateDto;
import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.adapter.dto.PlanPreviewDto;

import java.util.List;
import java.util.Optional;


public interface PlanService {
    public List<PlanDto> getAllPlans(); //모든 플랜 가져오기
    public List<PlanPreviewDto> getPlansByUserId(Long userId); //유저아이디별 플랜 가져오기
    public PlanDto createPlan(PlanCreateDto planDto, Long userIdFromToken); // 플랜 생성
    public PlanDto updatePlan(Long id,PlanCreateDto planCreateDto); //플랜 수정
    public void deletePlan(Long id); // 플랜 삭제
    public PlanDto getPlanById(Long planId); //플랜 상세 조회

    public Optional<PlanDto> getUpcomming(Long userId);
}
