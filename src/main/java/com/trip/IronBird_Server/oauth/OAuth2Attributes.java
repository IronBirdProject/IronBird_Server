package com.trip.IronBird_Server.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class OAuth2Attributes {
    String oauth2Id;
    String email;
    String name;
    String picture;
    Map<String, Object> attributes;
    String OauthType;

}
