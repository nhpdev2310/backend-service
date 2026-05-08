package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.common.RoleName;
import com.nhpdev.backendservice.dto.request.AssignPermissionRequest;
import com.nhpdev.backendservice.dto.request.RoleCreateRequest;
import com.nhpdev.backendservice.dto.response.AssignPermissionResponse;
import com.nhpdev.backendservice.dto.response.RoleDetailResponse;
import com.nhpdev.backendservice.entity.Role;


import java.util.List;

public interface RoleService {
    boolean existByRoleName(RoleName roleName);
    Role findRoleByName(RoleName roleName);
    RoleDetailResponse createRole(RoleCreateRequest request);
    List<RoleDetailResponse> getAllRoleWithPermission();
    AssignPermissionResponse assignPermission(String roleId, AssignPermissionRequest request);
    List<RoleDetailResponse> getAllRoleWithPermissionByRoleName(List<String> roleNames);
}
