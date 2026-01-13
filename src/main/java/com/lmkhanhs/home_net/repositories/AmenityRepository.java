package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.AmenityEntity;

@Repository
public interface AmenityRepository extends JpaRepository<AmenityEntity, Long> {
    boolean existsByNameAndTenantId(String name, String tenantId);
    Optional<AmenityEntity> findByNameAndTenantId(String name, String tenantId);
    Optional<AmenityEntity> findByIdAndTenantIdAndIsDeletedFalse(Long id, String tenantId);
    Optional<AmenityEntity> findByIdAndTenantIdAndIsDeletedTrue(Long id, String tenantId);
    Page<AmenityEntity> findAllByTenantIdAndIsDeletedFalse(String tenantId, Pageable pageable);
}
