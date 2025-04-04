package com.trip.IronBird_Server.map.infrastructure;

import com.trip.IronBird_Server.map.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
