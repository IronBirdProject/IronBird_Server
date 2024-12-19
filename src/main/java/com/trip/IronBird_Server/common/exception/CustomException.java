package com.trip.IronBird_Server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends  RuntimeException {
    private final ErrorCode errorCode;
}
