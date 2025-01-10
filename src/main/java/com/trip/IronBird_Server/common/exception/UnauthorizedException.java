package com.trip.IronBird_Server.common.exception;


/**
 * UnauthorizedExcption은 인증되지 않은 사용자나
 * 권한 부족으로 인해 발생하는 예외를 나타내는 커스텀 예외 클래스
 *
 * RunTImeException을 상속하여, Unchecked Excption으로 동작한다.
 */
public class UnauthorizedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }
}
