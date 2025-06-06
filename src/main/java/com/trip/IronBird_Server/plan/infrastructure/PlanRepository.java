package com.trip.IronBird_Server.plan.infrastructure;

import com.trip.IronBird_Server.plan.adapter.dto.PlanDto;
import com.trip.IronBird_Server.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findByUserId(Long userId);

    Optional<PlanDto> findFirstByUserIdAndStartedDateGreaterThanOrderByStartedDateAsc(Long userId, String today);
}
