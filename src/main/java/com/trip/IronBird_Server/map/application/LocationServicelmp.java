package com.trip.IronBird_Server.map.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.IronBird_Server.map.adapter.dto.LocationRequestDto;
import com.trip.IronBird_Server.map.adapter.dto.LocationResponseDto;
import com.trip.IronBird_Server.map.domain.Location;
import com.trip.IronBird_Server.map.infrastructure.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LocationServicelmp implements LocationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LocationRepository locationRepository;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Override
    public LocationResponseDto reverseGeoCoding(LocationRequestDto requestDto){
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/geo/coord2address.json" +
                    "?x=" + requestDto.getLongitude()
                    +"&y=" + requestDto.getLatitude();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization" , "KakaoAK " + kakaoApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);


            // kakao API 호출
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            String body = response.getBody();

            JsonNode root = objectMapper.readTree(body);
            JsonNode addressNode = root.path("documents").get(0).path("address");
            JsonNode roadNode = root.path("documents").get(0).path("road_address");

            String addressName = addressNode.path("address_name").asText();
            String region1 = addressNode.path("region_1depth_name").asText();
            String region2 = addressNode.path("region_2depth_name").asText();
            String region3 = addressNode.path("region_3depth_name").asText();
            String roadAddress = roadNode.isMissingNode() ? null : roadNode.path("address_name").asText();

            Location saved = locationRepository.save(Location.builder()
                            .latitude(requestDto.getLatitude())
                            .longitdue(requestDto.getLongitude())
                            .addressName(addressName)
                            .region1DepthName(region1)
                            .region2DepthName(region2)
                            .region2DepthName(region3)
                            .roadAddress(roadAddress)
                            .build());

            return LocationResponseDto.builder()
                    .addressName(saved.getAddressName())
                    .region1DepthName(saved.getRegion1DepthName())
                    .region2DepthName(saved.getRegion2DepthName())
                    .region3DepthName(saved.getRegion3DepthName())
                    .roadAddress(saved.getRoadAddress())
                    .build();

        } catch (Exception e){
            throw new RuntimeException("failed GeoCoding");
        }
    }


}
