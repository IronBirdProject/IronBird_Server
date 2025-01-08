package com.trip.IronBird_Server.plan.controller;

import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     @
     특정 유저 플랜 조회
     **/
    @GetMapping("/user/{userId}")
    public List<PlanDto> getPlansByUserId(@PathVariable("userId") Long userId) {    //PathVariable 에 명시적으로 userId 를 등록하여 경로 변수에 인식

        return planService.getPlansByUserId(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanDto planDto){
        PlanDto createdPlan = planService.createPlan(planDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }



}
