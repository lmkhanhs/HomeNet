package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByNameAndTenantId(String name, String tenantId);
    Optional<RoleEntity> findByIdAndTenantId(Long id, String tenantId);
}
