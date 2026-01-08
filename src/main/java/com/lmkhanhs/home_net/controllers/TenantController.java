package com.lmkhanhs.home_net.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.tenants.requests.CreateTenantRequest;
import com.lmkhanhs.home_net.dtos.tenants.responses.TenantResponse;
import com.lmkhanhs.home_net.services.TenantService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("${app.prefix}/auth/tenants")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TenantController {
    TenantService tenantService;
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    @PostMapping("")
    public ApiResponse<TenantResponse> createTenant(@RequestBody CreateTenantRequest request) {

        return ApiResponse.<TenantResponse>builder()
                .code(201)
                .message("Create tenant successfully")
                .data(tenantService.handleCreateTenant(request))
                .build();
    }
    
}
