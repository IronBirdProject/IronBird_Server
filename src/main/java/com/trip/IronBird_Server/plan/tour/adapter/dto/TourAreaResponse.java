package com.trip.IronBird_Server.plan.tour.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourAreaResponse {

    @JsonProperty("header")
    private TourAreaHeader header;

    @JsonProperty("body")
    private TourAreaBody body;


}
