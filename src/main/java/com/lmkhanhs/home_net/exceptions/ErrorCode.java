package com.lmkhanhs.home_net.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED(1000, "user existed", HttpStatus.CONFLICT),
    USERNAME_EXISTED(1001, "username existed", HttpStatus.CONFLICT),
    PERMISSION_EXISTED(1005, "permission existed", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND(1009, "permission not found", HttpStatus.BAD_REQUEST),
    TENANT_EXISTED(1006, "tenant existed", HttpStatus.CONFLICT),
    TENANT_NOT_FOUND(1007, "tenant not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1008, "user not found", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1010, "invalid credentials", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(1011, "role not found", HttpStatus.SERVICE_UNAVAILABLE),
    COUNTRY_EXISTED(2000, "country existed", HttpStatus.BAD_REQUEST),
    COUNTRY_NOT_FOUND(2001, "country not found", HttpStatus.BAD_REQUEST),

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
