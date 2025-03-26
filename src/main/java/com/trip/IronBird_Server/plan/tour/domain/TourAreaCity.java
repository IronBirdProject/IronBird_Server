package com.trip.IronBird_Server.plan.tour.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 하위 시군구 지역
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tourAreaCity")
@Builder
public class TourAreaCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rnum;

    private String code;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_area_id")
    private TourArea tourArea;




}
