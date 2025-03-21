package com.trip.IronBird_Server.plan.tour.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TourService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final ObjectMapper objectMapper;

    private TourService(RestTemplate restTemplate,
                        @Value("${tourapi.url}") String apiUrl,
                        ObjectMapper objectMapper){
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.objectMapper = objectMapper;
    }

    public TourAreaRoot getTourAreaList(){
        //API 호출
        TourAreaRoot response = restTemplate.getForObject(apiUrl, TourAreaRoot.class);

        //응답이 null or Error Code 반환시 예외 발생
        if(response == null || !"0000".equals(response.getResponse().getHeader().getResultCode())){
            throw new RuntimeException("관광정보 API 호출 실패");
        }

        //ObjectMapper를 사용하여 JSON 응답을 DTO로 변환
        if(!"0000".equals(response.getResponse().getHeader().getResultCode())){
            throw new RuntimeException("관광정보 API 호출 실패 : " + response.getResponse().getHeader().getResultMsg());
        }

        return response;

    }
}
