package com.trip.IronBird_Server.map.application;

import com.trip.IronBird_Server.map.adapter.dto.LocationRequestDto;
import com.trip.IronBird_Server.map.adapter.dto.LocationResponseDto;

public interface LocationService {

    public LocationResponseDto reverseGeoCoding(LocationRequestDto requestDto);
}
