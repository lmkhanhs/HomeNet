package com.lmkhanhs.home_net.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseTenantEntity {
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;
}