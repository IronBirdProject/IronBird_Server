package com.trip.IronBird_Server.plan.tour.infrastructure;

import com.trip.IronBird_Server.plan.tour.domain.TourArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourAreaRepository extends JpaRepository<TourArea, Long> {
    boolean existsByCode(String code);

    Optional<TourArea> findByName(String areaName);
}
