package com.lmkhanhs.home_net.services;

import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.users.responses.PermissionResponse;
import com.lmkhanhs.home_net.entities.PermissionEntity;
import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.PermissionMapper;
import com.lmkhanhs.home_net.repositories.PermissionRepository;
import com.lmkhanhs.home_net.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    PermissionMapper authMapper;

    public PermissionResponse handleCreatePermission(
        String tenantId,
        CreatePermissionRequest request,
        Long roleId
) {
    RoleEntity roleEntity = roleRepository
            .findByIdAndTenantId(roleId, tenantId)
            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

    permissionRepository.findByNameAndTenantId(request.getName(), tenantId)
            .ifPresent(p -> {
                throw new AppException(ErrorCode.PERMISSION_EXISTED);
            });

    PermissionEntity permission = authMapper.toEntity(request);
    permission.setTenantId(tenantId);

    // ✅ SET OWNING SIDE
    roleEntity.getPermissions().add(permission);

    // ✅ SYNC INVERSE SIDE
    permission.getRoles().add(roleEntity);

    // chỉ cần save 1 phía
    PermissionEntity saved = permissionRepository.save(permission);

    return authMapper.toResponse(saved);
}

}
