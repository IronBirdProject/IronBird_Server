package com.trip.IronBird_Server.map.infrastructure;

import com.trip.IronBird_Server.map.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Map, Long> {
}
