package com.trip.IronBird_Server.plan.adapter.controller;

import com.trip.IronBird_Server.common.custom.CustomUserDetails;
import com.trip.IronBird_Server.plan.adapter.dto.*;
import com.trip.IronBird_Server.plan.application.service.PlanService;
import com.trip.IronBird_Server.plan.application.service.PlanServiceImp;
import com.trip.IronBird_Server.plan.application.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final ScheduleService scheduleService;

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
//    @GetMapping("/user/{userId}")
//    public List<PlanDto> getPlansByUserId(@PathVariable("userId") Long userId) {    //PathVariable 에 명시적으로 userId 를 등록하여 경로 변수에 인식
//
//        return planService.getPlansByUserId(userId);
//    }

    @GetMapping("/user/{userId}")
    public List<PlanPreviewDto> getPlansByUserId(@PathVariable("userId") Long userId) {    //PathVariable 에 명시적으로 userId 를 등록하여 경로 변수에 인식
        log.info("Fetching plans for user with ID: {}", userId);
        return planService.getPlansByUserId(userId);
    }

    /**
     @
     플랜 상세 조회
     **/
    @GetMapping("/{planId}")
    public ResponseEntity<PlanDto> getPlanById(@PathVariable("planId") Long planId) {
        PlanDto planDto = planService.getPlanById(planId);
        return ResponseEntity.ok(planDto);
    }

    /**
     @
     플랜 생성 컨트롤러
     **/
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateDto planCreateDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){

        /// 임시 userId 1로 대체
//        Long userIdFromToken = userDetails.getId();
        PlanDto createdPlan = planService.createPlan(planCreateDto,1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }


    /**
     @
     특정 유저 플랜 수정
     **/
    @PutMapping("/update/{planId}")
    public ResponseEntity<PlanDto> updatePlanById(@PathVariable("planId") Long planId,
                                                  @RequestBody PlanCreateDto planCreateDto){
        log.info("Updating plan with ID: {}", planId);
        log.info("Plan details: {}", planCreateDto);
        PlanDto updatePlan = planService.updatePlan(planId, planCreateDto);
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

    /**
     * 스케쥴 생성 컨트롤러
     * @param planId
     * @param scheduleCreateDto
     * @return
     */
    @PostMapping("/{planId}/schedules")
    public ResponseEntity<ScheduleDto> createSchedule(@PathVariable("planId") Long planId,
                                                      @RequestBody ScheduleCreateDto scheduleCreateDto){
        ScheduleDto createSchedule = scheduleService.createSchedule(scheduleCreateDto, planId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createSchedule);
    }


}
