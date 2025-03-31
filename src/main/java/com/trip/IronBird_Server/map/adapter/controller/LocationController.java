package com.trip.IronBird_Server.map.adapter.controller;

import com.trip.IronBird_Server.map.adapter.dto.LocationRequestDto;
import com.trip.IronBird_Server.map.adapter.dto.LocationResponseDto;
import com.trip.IronBird_Server.map.application.LocationServicelmp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationServicelmp locationServicelmp;

    @PostMapping("/reverse-geo")
    public ResponseEntity<LocationResponseDto> reverseGeo(@RequestBody LocationRequestDto requestDto){
        LocationResponseDto result = locationServicelmp.reverseGeoCoding(requestDto);

        return ResponseEntity.ok(result);
    }
}
