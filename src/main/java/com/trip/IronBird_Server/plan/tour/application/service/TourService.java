package com.trip.IronBird_Server.plan.tour.application.service;

import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TourService {

    private final RestTemplate restTemplate;

    private final String apiUrl;

    private TourService(RestTemplate restTemplate,
                        @Value("${tourapi.url}") String apiUrl){
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public TourAreaRoot getTourAreaList(){
        //API 호출
        TourAreaRoot response = restTemplate.getForObject(apiUrl, TourAreaRoot.class);

        if(response == null || !"0000".equals(response.getResponse().getHeader().getResultCode())){
            throw new RuntimeException("관광정보 API 호출 실패");
        }

        return response;

    }
}
