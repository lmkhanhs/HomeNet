package com.lmkhanhs.home_net.controllers;

import java.nio.file.Path;

import org.apache.catalina.connector.Request;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmkhanhs.home_net.dtos.apps.ApiResponse;
import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.users.responses.PermissionResponse;
import com.lmkhanhs.home_net.services.PermissionService;
import com.lmkhanhs.home_net.utils.RequestHttpUitlls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${app.prefix}/permissions")
public class PermissionController {
    PermissionService permissionService;
    RequestHttpUitlls requestHttpUitlls;


    @PostMapping("/{roleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PermissionResponse> createPermission(@RequestBody CreatePermissionRequest request, HttpServletRequest httpServletRequest, @PathVariable Long roleId) {
        String tenantID = this.requestHttpUitlls.getTenantIDValid(httpServletRequest);
        return ApiResponse.<PermissionResponse>builder()
                .code(201)
                .message("Create permission successfully!")
                .data(this.permissionService.handleCreatePermission(tenantID,request, roleId))
                .build();
    }
}
