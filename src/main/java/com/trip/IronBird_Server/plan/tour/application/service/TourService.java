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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TourService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String cityApiUrl;
    private final String serviceKey;
    private final ObjectMapper objectMapper;
    private final TourAreaRepository tourAreaRepository;

    public TourService(RestTemplate restTemplate,
                       @Value("${tourapi.url}") String apiUrl,
                       @Value("${tourapi.service-key}") String serviceKey,
                       @Value("${tourapicity.url}" ) String cityApiUrl,
                       ObjectMapper objectMapper, TourAreaRepository tourAreaRepository) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.cityApiUrl = cityApiUrl;
        this.serviceKey = serviceKey;
        this.objectMapper = objectMapper;
        this.tourAreaRepository = tourAreaRepository;
    }


    /**
     * 지역코드 파싱
     * @return
     */
    public TourAreaRoot getTourAreaList() {
        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String fullUrl = apiUrl + "?serviceKey=" + encodedServiceKey + "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
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


    /**
     * 지역 시군구 파싱
     * @return
     */

    public Map<String, List<TourAreaItem>> getTourAreaCitiesGrouped() {
        Map<String, List<TourAreaItem>> cityMap = new LinkedHashMap<>(); // 순서 보장

        // 1. 지역 코드와 이름 매핑
        Map<Integer, String> areaCodeNameMap = Map.ofEntries(
                Map.entry(1, "서울"),
                Map.entry(2, "인천"),
                Map.entry(3, "대전"),
                Map.entry(4, "대구"),
                Map.entry(5, "광주"),
                Map.entry(6, "부산"),
                Map.entry(7, "울산"),
                Map.entry(8, "세종특별자치시"),
                Map.entry(31, "경기도"),
                Map.entry(32, "강원특별자치도"),
                Map.entry(33, "충청북도"),
                Map.entry(34, "충청남도"),
                Map.entry(35, "경상북도"),
                Map.entry(36, "경상남도"),
                Map.entry(37, "전북특별자치도"),
                Map.entry(38, "전라남도"),
                Map.entry(39, "제주특별자치도")
        );

        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

            for (Map.Entry<Integer, String> entry : areaCodeNameMap.entrySet()) {
                int areaCode = entry.getKey();
                String areaName = entry.getValue();

                String fullCityUrl = cityApiUrl
                        + "?serviceKey=" + encodedServiceKey
                        + "&areaCode=" + areaCode
                        + "&numOfRows=100"
                        + "&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";

                log.info("Requesting AreaCode: {} ({})", areaCode, areaName);
                URI url = new URI(fullCityUrl);
                String response = restTemplate.getForObject(url, String.class);

                if (response.startsWith("<")) {
                    log.error("API 에러 응답: {}", response);
                    continue;
                }

                TourAreaRoot result = objectMapper.readValue(response, TourAreaRoot.class);

                if (result == null || result.getResponse() == null) {
                    log.error("{} 응답 구조가 잘못되었습니다", areaName);
                    continue;
                }

                if (!"0000".equals(result.getResponse().getHeader().getResultCode())) {
                    log.error("{} Tour API 호출 실패: {}", areaName, result.getResponse().getHeader().getResultMsg());
                    continue;
                }

                List<TourAreaItem> items = result.getResponse().getBody().getItems().getItem();
                if (items != null) {
                    cityMap.put(areaName, items);
                }
            }

            return cityMap;

        } catch (Exception e) {
            throw new RuntimeException("Tour API 호출 또는 파싱 실패", e);
        }
    }




}
