package com.lmkhanhs.home_net.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permissions")
public class PermissionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Builder.Default
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    Set<RoleEntity> roles = new HashSet<>();
}
