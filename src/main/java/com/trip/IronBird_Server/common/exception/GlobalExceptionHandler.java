package com.trip.IronBird_Server.common.exception;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //Reqeust Body Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE,
                exception.getBindingResult().getFieldError().getDefaultMessage());
    }

    //Reqeust Param or ModelAttribute Validation Exception
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(BindException exception) {
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE,
                exception.getBindingResult().getFieldError().getDefaultMessage());
    }

//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
//        return ErrorResponse.toResponseEntity(exception.getErrorCode());
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("IllegalArgumentException: {}", e.getMessage());
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}