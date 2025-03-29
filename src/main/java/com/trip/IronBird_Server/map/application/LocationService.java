package com.trip.IronBird_Server.map.application;

import com.trip.IronBird_Server.map.infrastructure.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;



}
