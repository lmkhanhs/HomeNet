package com.lmkhanhs.home_net.services;

import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.dtos.tenants.requests.CreateTenantRequest;
import com.lmkhanhs.home_net.dtos.tenants.responses.TenantResponse;
import com.lmkhanhs.home_net.entities.TenantEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.TenantMapper;
import com.lmkhanhs.home_net.repositories.TenantRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TenantService {
    TenantRepository tenantRepository;
    TenantMapper tenantMapper;

    public TenantResponse handleCreateTenant(CreateTenantRequest request) {
        String name = request.getName();
        if (this.tenantRepository.existsByName(name)) {
            throw new AppException(ErrorCode.TENANT_EXISTED, "Please choose another tenant name!");
        }
        TenantEntity tenantEntity = this.tenantMapper.toEntity(request);
        tenantEntity = this.tenantRepository.save(tenantEntity);
        return this.tenantMapper.toResponse(tenantEntity);
    }
}
