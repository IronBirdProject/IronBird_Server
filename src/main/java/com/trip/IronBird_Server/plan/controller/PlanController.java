package com.trip.IronBird_Server.plan.controller;

import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    /**
     @
     모든 플랜 조회
     **/
    @GetMapping
    public List<PlanDto> getAllPlans(){

        return planService.getAllPlans();
    }

}
