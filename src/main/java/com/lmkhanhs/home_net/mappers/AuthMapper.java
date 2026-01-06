package com.lmkhanhs.home_net.mappers;

import java.security.Permission;

import org.mapstruct.Mapper;

import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.auth.requests.CreateRoleRequest;
import com.lmkhanhs.home_net.dtos.auth.responses.PermissionResponse;
import com.lmkhanhs.home_net.dtos.auth.responses.RoleResponse;
import com.lmkhanhs.home_net.entities.PermissionEntity;
import com.lmkhanhs.home_net.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    RoleEntity toEntity(CreateRoleRequest request);
    RoleResponse toResponse(RoleEntity entity);

    PermissionEntity toEntity(CreatePermissionRequest request);
    PermissionResponse toResponse(PermissionEntity entity);
}

