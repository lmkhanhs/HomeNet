package com.lmkhanhs.home_net.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "Roles")
public class RoleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    Set<UserEntity> users;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "role-permissions",
               joinColumns = @JoinColumn(name = "role_id"), 
               inverseJoinColumns = @JoinColumn(name = "permission_id"))
    Set<PermissionEntity> permissions = new java.util.HashSet<>();
}