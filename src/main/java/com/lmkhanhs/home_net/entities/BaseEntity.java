package com.lmkhanhs.home_net.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity extends BaseTenantEntity {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
