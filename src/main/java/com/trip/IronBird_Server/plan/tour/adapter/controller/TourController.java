package com.trip.IronBird_Server.plan.tour.adapter.controller;

import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaRoot;
import com.trip.IronBird_Server.plan.tour.application.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/areas")
    public ResponseEntity<?> getTourList(){
        TourAreaRoot tourAreaRoot = tourService.getTourAreaList();

        return ResponseEntity.ok(tourAreaRoot.getResponse().getBody().getItems());
    }
}
