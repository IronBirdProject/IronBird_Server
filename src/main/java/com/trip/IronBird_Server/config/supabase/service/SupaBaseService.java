package com.trip.IronBird_Server.config.supabase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupaBaseService {


    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate;

    /**
     * 데이터 조회
     */
    public List<Map<String, Object>> getAllFromTable(String tableName){
        String url = supabaseUrl + "/rest/v1/" + tableName;

        //해더설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseApiKey);
        headers.set("Authorization", "Bearer " + supabaseApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        //API 호출
        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        return response.getBody();
    }


    /**
     * 데이터 추가
     */
    public Map<String, Object> addToTable(String tableName, Map<String, Object> data) {
        String url = supabaseUrl + "/rest/v1/" + tableName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseApiKey);
        headers.set("Authorization", "Bearer " + supabaseApiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Prefer", "return=representation"); // 생성된 데이터 반환

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        return response.getBody();
    }

}
