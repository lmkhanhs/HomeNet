package com.lmkhanhs.home_net.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.PermissionResponse;
import com.lmkhanhs.home_net.dtos.users.responses.RoleResponse;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.PermissionEntity;
import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.entities.UserEntity;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserEntity toEntity(CreateUserRequest request);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "toRolesResponse")
    public abstract UserResponse toResponse(UserEntity entity);

    @Named("toRolesResponse")
    protected Set<RoleResponse> toRolesResponse(Set<RoleEntity> entities) {
        Helper helper = new Helper();
        Set<RoleResponse> responses = new java.util.HashSet<>();
        for (RoleEntity entity : entities) {
            responses.add(helper.toRoleResponse(entity));
        }
        return responses;
    }
}

class Helper {
    public PermissionResponse toPermissionResponse(PermissionEntity entity) {
        PermissionResponse response = new PermissionResponse();
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPermissionId(Long.toString(entity.getId()));
        return response;
    }   
    public RoleResponse toRoleResponse(RoleEntity entity) {
        RoleResponse response = new RoleResponse();
        response.setName(entity.getName());
        response.setRoleId(Long.toString(entity.getId()));
        java.util.Set<PermissionResponse> permissionResponses = new java.util.HashSet<>();
        for (PermissionEntity permissionEntity : entity.getPermissions()) {
            permissionResponses.add(toPermissionResponse(permissionEntity));
        }
        response.setPermissions(permissionResponses);
        return response;
    }
}