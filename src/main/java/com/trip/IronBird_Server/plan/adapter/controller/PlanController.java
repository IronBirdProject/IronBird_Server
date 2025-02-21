package com.trip.IronBird_Server.plan.adapter.controller;

import com.trip.IronBird_Server.common.custom.CustomUserDetails;
import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.application.service.PlanServiceImp;
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

    private final PlanServiceImp planServiceImp;

    /**
     @
     모든 플랜 조회
     **/
    @GetMapping
    public List<PlanDto> getAllPlans(){

        return planServiceImp.getAllPlans();
    }

    /**
     @
     특정 유저 플랜 조회
     **/
    @GetMapping("/user/{userId}")
    public List<PlanDto> getPlansByUserId(@PathVariable("userId") Long userId) {    //PathVariable 에 명시적으로 userId 를 등록하여 경로 변수에 인식

        return planServiceImp.getPlansByUserId(userId);
    }

    /**
     @
     플랜 생성 컨트롤러
     **/
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanDto planDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){

        Long userIdFromToken = userDetails.getId();
        PlanDto createdPlan = planServiceImp.createPlan(planDto,userIdFromToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }


    /**
     @
     특정 유저 플랜 수정
     **/
    @PutMapping("/update/{planId}")
    public ResponseEntity<PlanDto> updatePlanById(@PathVariable("planId") Long planId,
                                                  @RequestBody PlanDto planDto){
        PlanDto updatePlan = planServiceImp.updatePlan(planId, planDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatePlan);
    }

    /**
     @
     특정 유저 플랜 삭제
     **/
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable("planId") Long PlanId){
        try {
            planServiceImp.deletePlan(PlanId);

            return ResponseEntity.ok("플랜이 삭제되었습니다.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("플랜이 삭제되었습니다.");
        }
    }


}
