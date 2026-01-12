package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.CategoryEntity;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByNameAndTenantId(String name, String tenantId);
    Optional<CategoryEntity> findByIdAndTenantIdAndIsDeletedFalse(Long id, String tenantId);
    Optional<CategoryEntity> findByIdAndTenantIdAndIsDeletedTrue(Long id, String tenantId);
    Page<CategoryEntity> findAllByTenantIdAndIsDeletedFalse(String tenantId, Pageable pageable);
}
