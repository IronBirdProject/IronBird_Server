package com.trip.IronBird_Server.jwt.dto;

public record JwtDto(
        String accessToken,
        String refreshToekn
) {
}
