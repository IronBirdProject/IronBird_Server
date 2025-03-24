package com.trip.IronBird_Server.plan.tour.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trip.IronBird_Server.common.custom.ItemsDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourAreaBody {

    @JsonProperty("items")
    @JsonDeserialize(using = ItemsDeserializer.class)
    private TourAreaItems items;

    @JsonProperty("numOfRows")
    private int numOfRows;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("totalCount")
    private int totalCount;
}

