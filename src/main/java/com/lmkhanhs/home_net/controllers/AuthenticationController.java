package com.lmkhanhs.home_net.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.auth.requests.LoginRequest;
import com.lmkhanhs.home_net.dtos.auth.requests.LogoutRequest;
import com.lmkhanhs.home_net.dtos.auth.responses.LoginResponse;
import com.lmkhanhs.home_net.dtos.auth.responses.LogoutResponse;
import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.services.AuthenticationService;
import com.lmkhanhs.home_net.utils.RequestHttpUitlls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${app.prefix}/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    RequestHttpUitlls requestHttpUitlls;
    

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        String tenantID = this.requestHttpUitlls.getTenantIDValid(httpServletRequest);
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login successfully!")
                .data(this.authenticationService.handleLogin(tenantID, request))
                .build();
    }
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody CreateUserRequest request, HttpServletRequest httpServletRequest) {
        String tenantID = this.requestHttpUitlls.getTenantIDValid(httpServletRequest);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Register successfully!")
                .data(this.authenticationService.handRegister(tenantID, request))
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@RequestBody LogoutRequest logoutRequest) {
        return ApiResponse.<LogoutResponse>builder()
                .code(200)
                .message("Logout successfully!")
                .data(this.authenticationService.handLogout(logoutRequest))
                .build();
    }
    
}
