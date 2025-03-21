package com.trip.IronBird_Server.plan.tour.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourAreaItems {

    @JsonProperty("item")
    private List<TourAreaItem> item;
}
