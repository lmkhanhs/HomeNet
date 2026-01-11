package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.CountryEntity;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findByNameAndTenantId(String name, String tenantId);
    Page<CountryEntity> findAllByTenantIdAndIsDeletedFalse(String tenantId, org.springframework.data.domain.Pageable pageable);
}
