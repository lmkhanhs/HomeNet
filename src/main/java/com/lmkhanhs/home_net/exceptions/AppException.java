package com.lmkhanhs.home_net.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode; 
    private String resolution = "Please contact to admin for support!";

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.resolution = "Please contact to admin for support!";
    }

    public AppException(ErrorCode errorCode, String resolution) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.resolution = resolution;
    }   
}
