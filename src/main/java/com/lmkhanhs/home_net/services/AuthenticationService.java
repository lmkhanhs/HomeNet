package com.lmkhanhs.home_net.services;

import java.security.Permission;

import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.auth.responses.PermissionResponse;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.AuthMapper;
import com.lmkhanhs.home_net.repositories.PermissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    // Add fields and methods here
    PermissionRepository permissionRepository;
    AuthMapper authMapper;

    public PermissionResponse handleCreatePermission(CreatePermissionRequest request) {
        permissionRepository.findByName(request.getName())
                .ifPresent(p -> {
                    throw new AppException(ErrorCode.PERMISSION_EXISTED);
                });
        var permission = this.authMapper.toEntity(request);
        return this.authMapper.toResponse(
                this.permissionRepository.save(permission));
    }
}
