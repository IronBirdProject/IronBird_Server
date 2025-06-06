package com.trip.IronBird_Server.plan.infrastructure;

import com.trip.IronBird_Server.plan.domain.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findByUserId(Long userId);

    Optional<Plan> findFirstByUserIdAndStartedDateGreaterThanOrderByStartedDateAsc(Long userId, String today);

}
