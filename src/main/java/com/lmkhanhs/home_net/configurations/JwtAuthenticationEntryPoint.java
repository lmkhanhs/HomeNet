package com.lmkhanhs.home_net.configurations;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmkhanhs.home_net.dtos.apps.ErrorResponse;
import com.lmkhanhs.home_net.exceptions.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_EXCEPTION;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .resolution("Please check your logins and try again.")
                .build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(responseBody);
        response.flushBuffer();
    }

    
}
