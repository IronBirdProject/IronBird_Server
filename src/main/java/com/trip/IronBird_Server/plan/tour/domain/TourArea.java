package com.trip.IronBird_Server.plan.tour.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *  상위 지역 코드
 */

@Entity
@Table(name = "tour_area")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rnum;

    private String code;

    private String name;

    @OneToMany(mappedBy = "tourArea", cascade = CascadeType.ALL)
    private List<TourAreaCity> tourAreaCities = new ArrayList<>();

}
