package com.trip.IronBird_Server.plan.tour.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaItem;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaRoot;
import com.trip.IronBird_Server.plan.tour.domain.Tour;
import com.trip.IronBird_Server.plan.tour.infrastructure.TourAreaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class TourService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String serviceKey;
    private final ObjectMapper objectMapper;

    private final TourAreaRepository tourAreaRepository;

    public TourService(RestTemplate restTemplate,
                       @Value("${tourapi.url}") String apiUrl,
                       @Value("${tourapi.service-key}") String serviceKey,
                       ObjectMapper objectMapper, TourAreaRepository tourAreaRepository) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.serviceKey = serviceKey;
        this.objectMapper = objectMapper;
        this.tourAreaRepository = tourAreaRepository;
    }

    public TourAreaRoot getTourAreaList() {
        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String fullUrl = apiUrl + "?serviceKey=" + encodedServiceKey + "&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
            URI uri = new URI(fullUrl);

            log.info("Request URL: {}", uri);

            // API 호출
            String responseStr = restTemplate.getForObject(uri, String.class);
            log.info("Response: {}", responseStr);

            // JSON 파싱
            TourAreaRoot result = objectMapper.readValue(responseStr, TourAreaRoot.class);

            //DB 저장
            List<TourAreaItem> items = result.getResponse().getBody().getItems().getItem();
            for (TourAreaItem item : items) {

                    Tour tourArea = Tour.builder()
                            .rnum(item.getRnum())
                            .code(item.getCode())
                            .name(item.getName())
                            .build();

                    tourAreaRepository.save(tourArea);
            }

            if (result == null || result.getResponse() == null) {
                throw new RuntimeException("응답 구조가 잘못되었습니다 (response null)");
            }

            if (!"0000".equals(result.getResponse().getHeader().getResultCode())) {
                throw new RuntimeException("Tour API 호출 실패: " + result.getResponse().getHeader().getResultMsg());
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Tour API 호출 또는 파싱 실패", e);
        }
    }

}
