package com.trip.IronBird_Server.user.domain.modeltype;


import java.util.Arrays;

public enum OauthType {

    Kakao("kakao");

    private final String type;

    OauthType(String type) {
        this.type = type;
    }

    public static OauthType ofType(String type) {
        return Arrays.stream(values())
                .filter(oauthType -> oauthType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such OauthType"));
    }

    public String getType() {
        return type;
    }
}
