package com.trip.IronBird_Server.plan.tour.infrastructure;

import com.trip.IronBird_Server.plan.tour.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourAreaRepository extends JpaRepository<Tour, Long> {
    boolean existsByCode(String code);
}
