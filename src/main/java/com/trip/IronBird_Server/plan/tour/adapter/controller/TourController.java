package com.trip.IronBird_Server.plan.tour.adapter.controller;

import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaItem;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaItems;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaRoot;
import com.trip.IronBird_Server.plan.tour.application.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/areas")
    public ResponseEntity<?> getTourList(){
        TourAreaRoot tourAreaRoot = tourService.getTourAreaList();
        return ResponseEntity.ok(tourAreaRoot); // 전체 반환
    }

    @GetMapping("/CityArea/save")
    public ResponseEntity<String> saveTourCityList() {
        tourService.getTourAreaCitiesGroupedAndSave();
        return ResponseEntity.ok("지역 코드 및 시군구 저장 완료");
    }




}
