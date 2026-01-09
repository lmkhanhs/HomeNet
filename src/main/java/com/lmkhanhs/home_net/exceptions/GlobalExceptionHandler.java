package com.lmkhanhs.home_net.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.apps.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        
        ErrorCode errorCode = ex.getErrorCode();
        String resolution = ex.getResolution();
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .resolution(resolution)
                .build();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
    
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    ex.printStackTrace();

    ErrorResponse errorResponse = ErrorResponse.builder()
            .code(500)  
            .message("RuntimeException")
            .resolution("Please contact to admin for support!")
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_EXCEPTION;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .resolution("Please check your permissions and try again.")
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus().value()).body(errorResponse);
    }
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_EXCEPTION;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .resolution("Please check your logins and try again.")
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus().value()).body(errorResponse);
    }
    
}
