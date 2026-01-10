package com.lmkhanhs.home_net.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lmkhanhs.home_net.entities.TenantEntity;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, String> {
    boolean existsByName(String name);
    Optional<TenantEntity> findByName(String name);

}
