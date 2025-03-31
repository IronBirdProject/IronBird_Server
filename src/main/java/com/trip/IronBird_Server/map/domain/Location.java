package com.trip.IronBird_Server.map.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude; // 경도
    private Double longitdue; // 위도

    private String addressName; //전체 주소
    private String regionCityName; // 시/도 (ex: 서울특별시)
    private String regionTownName;  // 시/군/구 (ex: 종로구)
    private String regionVillageName; // 동/읍/면 (ex: 세종로)

    private String roadAddress; // 도로명 주소

}
