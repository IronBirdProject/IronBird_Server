package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.dto.PlanCreateDto;
import com.trip.IronBird_Server.plan.adapter.dto.PlanPreviewDto;
import com.trip.IronBird_Server.plan.adapter.mapper.PlanMapper;
import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.infrastructure.PlanRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlanServiceImp implements PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMapper planMapper;

    /**
     * 모든 플랜 가져오기
     * @return
     */
    @Override
    public List<PlanDto> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 유저의 플랜 조회
     * @param userId
     * @return
     */
    @Override
    public List<PlanPreviewDto> getPlansByUserId(Long userId){
        List<PlanPreviewDto> planPreviewDtos = planRepository.findByUserId(userId).stream()
                .map(planMapper::toDtoPreview)
                .collect(Collectors.toList());
        log.info(planPreviewDtos.toString());
        return planPreviewDtos;
    }


    /**
     * 플랜 생성 서비스
     * @param planCreateDto
     * @param userIdFromToken
     * @return
     */
    @Override
    public PlanDto createPlan(PlanCreateDto planCreateDto, Long userIdFromToken){
        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userIdFromToken));


        Plan plan = Plan.builder()
                .title(planCreateDto.getTitle())
                .destination(planCreateDto.getDestination())
                .startedDate(planCreateDto.getStartedDate())
                .endDate(planCreateDto.getEndDate())
                .user(user) //user 설정
                .build();


        //User 는 외부에서 가져오기
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
    }


    //플랜 수정 서비스
    @Override
    public PlanDto updatePlan(Long id,PlanCreateDto planCreateDto){

        //기존 플랜 조회
        Plan exitPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan Not found with ID : " + id));


//        //변경할 데이터 가져와서 엔티티에 등록
//        //exitPlan.updateDates(planCreateDto.getDestination(), planCreateDto.getStartedDate(), planCreateDto.getEndDate());
//
//        //수정된 데이터 저장
//        Plan updatePlan = planRepository.save(exitPlan);

        if (planCreateDto.getTitle() != null) {
            exitPlan.setTitle(planCreateDto.getTitle());
        }
        if (planCreateDto.getStartedDate() != null) {
            exitPlan.setStartedDate(planCreateDto.getStartedDate());
        }
        if (planCreateDto.getEndDate() != null) {
            exitPlan.setEndDate(planCreateDto.getEndDate());
        }

        // 수정된 플랜 저장
        Plan updatePlan = planRepository.save(exitPlan);

        // 저장된 결과를 PlanDto로 변환하여 반환
        return planMapper.toDto(updatePlan);
    }

    //플랜 삭제 서비스
    @Override
    public void deletePlan(Long id){
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        planRepository.delete(plan);
    }

    @Override
    public PlanDto getPlanById(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 플랜을 찾을 수 없습니다. : " + planId));

        return planMapper.toDto(plan);
    }

    @Override
    public Optional<PlanDto> getUpcomming(Long userId) {
        String today = LocalDateTime.now().toString();
        return planRepository.findFirstByUserIdAndStartedDateGreaterThanOrderByStartedDateAsc(userId, today);
    }


}
