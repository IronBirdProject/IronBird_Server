package com.trip.IronBird_Server.plan.application.service;

import com.trip.IronBird_Server.plan.adapter.mapper.PlanMapper;
import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.infrastructure.PlanRepository;
import com.trip.IronBird_Server.plan.infrastructure.ScheduleRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<PlanDto> getPlansByUserId(Long userId){
        return planRepository.findByUserId(userId).stream()
                .map(planMapper::toDto)
                .collect(Collectors.toList());
    }


    /**
     * 플랜 생성 서비스
     * @param planDto
     * @param userIdFromToken
     * @return
     */
    @Override
    public PlanDto createPlan(PlanDto planDto, Long userIdFromToken){
        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userIdFromToken));


        Plan plan = Plan.builder()
                .destination(planDto.getDestination())
                .startedDate(planDto.getStartedDate())
                .endDate(planDto.getEndDate())
                .user(user) //user 설정
                .build();


        //User 는 외부에서 가져오기
        Plan savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
    }


    //플랜 수정 서비스
    @Override
    public PlanDto updatePlan(Long id,PlanDto planDto){

        //기존 플랜 조회
        Plan exitPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan Not found with ID : " + id));


        //변경할 데이터 가져와서 엔티티에 등록
        //exitPlan.updateDates(planDto.getDestination(), planDto.getStartedDate(), planDto.getEndDate());

        //수정된 데이터 저장
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

    /**
    유저가 설정한 가장
    빠른 여행일정 넘겨오는 로직
     **/
    @Override
    public Optional<Plan> upcommingPlan(Long userid){
        String today = LocalDateTime.now().toString();
        return planRepository.findFirstByUserIdAndStartedDateGreaterThanOrderByStartedDateAsc(userid, today);
    }

}
