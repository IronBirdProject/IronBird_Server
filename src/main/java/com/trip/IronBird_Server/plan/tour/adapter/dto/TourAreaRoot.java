package com.trip.IronBird_Server.plan.tour.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourAreaRoot {

    @JsonProperty("response")
    private TourAreaResponse response;

}
