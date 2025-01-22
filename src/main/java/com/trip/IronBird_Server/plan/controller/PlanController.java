package com.trip.IronBird_Server.plan.controller;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.dto.PlanDto;
import com.trip.IronBird_Server.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     @
     플랜 생성 컨트롤러
     **/
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanDto planDto,
                                        @AuthenticationPrincipal Long userIdFromToken){
        PlanDto createdPlan = planService.createPlan(planDto,userIdFromToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }


    /**
     @
     특정 유저 플랜 수정
     **/
    @PutMapping("/update/{planId}")
    public ResponseEntity<PlanDto> updatePlanById(@PathVariable("planId") Long planId,
                                                  @RequestBody PlanDto planDto,
                                                  @AuthenticationPrincipal Long userIdFromToken){
        PlanDto updatePlan = planService.updatePlan(planId, planDto,userIdFromToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatePlan);
    }

    /**
     @
     특정 유저 플랜 삭제
     **/
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable("planId") Long PlanId){
        try {
            planService.deletePlan(PlanId);

            return ResponseEntity.ok("플랜이 삭제되었습니다.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("플랜이 삭제되었습니다.");
        }
    }


}
