package com.lmkhanhs.home_net.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lmkhanhs.home_net.dtos.tenants.requests.CreateTenantRequest;
import com.lmkhanhs.home_net.dtos.tenants.responses.TenantResponse;
import com.lmkhanhs.home_net.entities.TenantEntity;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    TenantEntity toEntity(CreateTenantRequest request);
    @Mapping(source = "id", target = "_id")
    TenantResponse toResponse(TenantEntity entity);
}
