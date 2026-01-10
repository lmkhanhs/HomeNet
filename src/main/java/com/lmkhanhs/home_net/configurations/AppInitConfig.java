package com.lmkhanhs.home_net.configurations;

import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.entities.TenantEntity;
import com.lmkhanhs.home_net.entities.UserEntity;
import com.lmkhanhs.home_net.enums.RoleEnum;
import com.lmkhanhs.home_net.repositories.PermissionRepository;
import com.lmkhanhs.home_net.repositories.RoleRepository;
import com.lmkhanhs.home_net.repositories.TenantRepository;
import com.lmkhanhs.home_net.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppInitConfig {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    PermissionRepository permissionRepository;
    TenantRepository tenantRepository;

    @Bean
    ApplicationRunner appInit() {
        return args -> {
            createTenantDefault();
            createRole();
            createAdminUser();
        };
    }
    @Transactional
    void createTenantDefault() {
        // tenant default VNPT name
        tenantRepository.findByName("VNPT")
                .orElseGet(() -> tenantRepository.save(
                        com.lmkhanhs.home_net.entities.TenantEntity.builder().name("VNPT").build()
                ));
    }

    @Transactional
    void createRole() {
        TenantEntity tenant = tenantRepository.findByName("VNPT").get();
        for (RoleEnum role : RoleEnum.values()) {
            roleRepository.findByName(role.name())
                    .orElseGet(() -> roleRepository.save(
                            RoleEntity.builder().tenantId(tenant.getName()).name(role.name()).build()
                    ));
        }
    }

    @Transactional
    void createAdminUser() {
        if (userRepository.findByUsername("admin").isPresent())
            return;
        TenantEntity tenant = tenantRepository.findByName("VNPT").get();
    
        Set<RoleEntity> roles = Set.of(
                roleRepository.findByName(RoleEnum.ADMIN.name()).orElseThrow(),
                roleRepository.findByName(RoleEnum.USER.name()).orElseThrow(),
                roleRepository.findByName(RoleEnum.HOST.name()).orElseThrow());

        userRepository.save(
                UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("Administrator")
                        .roles(roles)
                        .tenantId(tenant.getName())
                        .isActive(true)
                        .avatarUrl("http://localhost:8088/images/a279be82-eeba-4091-b05f-85bdf3c236f3.jpg")
                        .build());
    }
}
