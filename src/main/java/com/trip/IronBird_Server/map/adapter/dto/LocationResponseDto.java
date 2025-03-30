package com.trip.IronBird_Server.map.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponseDto {

    private String addressName; //전체 주소
    private String region1DepthName; // 시/도 (ex: 서울특별시)
    private String region2DepthName;  // 시/군/구 (ex: 종로구)
    private String region3DepthName; // 동/읍/면 (ex: 세종로)
    private String roadAddress;

}
