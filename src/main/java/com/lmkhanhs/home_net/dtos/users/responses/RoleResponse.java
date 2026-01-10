package com.lmkhanhs.home_net.dtos.users.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {
    String roleId;
    String name;
    java.util.Set<PermissionResponse> permissions;
}
