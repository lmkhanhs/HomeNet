package com.lmkhanhs.home_net.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.users.responses.PermissionResponse;
import com.lmkhanhs.home_net.entities.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionEntity toEntity(CreatePermissionRequest request);
    @Mapping(target = "permissionId", source = "id")
    PermissionResponse toResponse(PermissionEntity entity);
}

