package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    boolean existsByName(String name);
    Optional<PermissionEntity> findByName(String name);
    Optional<PermissionEntity> findByNameAndTenantId(String name, String tenantId);
}
