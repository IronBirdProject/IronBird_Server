package com.trip.IronBird_Server.common.custom;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.IronBird_Server.plan.tour.adapter.dto.TourAreaItems;

import java.io.IOException;

public class ItemsDeserializer extends JsonDeserializer<TourAreaItems> {
    @Override
    public TourAreaItems deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // 빈 문자열 처리
        if(node.isTextual() && node.asText().isEmpty()){
            return new TourAreaItems();
        }

        // 정상 데이터인 경우 기본 파싱
        ObjectMapper mapper  =(ObjectMapper) jsonParser.getCodec();
        return mapper.treeToValue(node, TourAreaItems.class);
    }
}
