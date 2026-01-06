package com.lmkhanhs.home_net.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED(1000, "user existed", HttpStatus.CONFLICT),
    USERNAME_EXISTED(1001, "username existed", HttpStatus.CONFLICT),
    PERMISSION_EXISTED(1005, "permission existed", HttpStatus.CONFLICT),

    AUTHENTICATION_EXCEPTION(1002, "authentication exception", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_EXCEPTION(1003, "unauthorized exception {User don't have permission!}", HttpStatus.FORBIDDEN),
    JWT_EXCEPTION(1004, "jwt exception", HttpStatus.UNAUTHORIZED);
    

    // Fields
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
