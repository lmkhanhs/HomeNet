package com.lmkhanhs.home_net.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.auth.responses.PermissionResponse;
import com.lmkhanhs.home_net.services.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${app.prefix}/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PermissionResponse> createPermission(@RequestBody CreatePermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .code(201)
                .message("Create permission successfully!")
                .data(this.authenticationService.handleCreatePermission(request))
                .build();
    }
}
