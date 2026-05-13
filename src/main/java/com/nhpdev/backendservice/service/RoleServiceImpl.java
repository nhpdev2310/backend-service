package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.common.RoleName;
import com.nhpdev.backendservice.dto.request.AssignPermissionRequest;
import com.nhpdev.backendservice.dto.request.RoleCreateRequest;
import com.nhpdev.backendservice.dto.response.AssignPermissionResponse;
import com.nhpdev.backendservice.dto.response.RoleDetailResponse;
import com.nhpdev.backendservice.entity.Permission;
import com.nhpdev.backendservice.entity.Role;
import com.nhpdev.backendservice.entity.RoleHasPermission;
import com.nhpdev.backendservice.exception.BackendServiceException;
import com.nhpdev.backendservice.exception.ErrorCode;
import com.nhpdev.backendservice.repository.PermissionRepository;
import com.nhpdev.backendservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    @Override
    public boolean existByRoleName(RoleName roleName) {
        return roleRepository.existsByName(roleName.name());
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByName(RoleName roleName) {
        return roleRepository.findRoleByName(roleName.name())
                .orElseThrow(() -> new BackendServiceException(ErrorCode.ROLE_NOT_EXISTED));
    }

    @Override
    @Transactional
    public RoleDetailResponse createRole(RoleCreateRequest request) {
        if(roleRepository.existsByName(request.name()))
            throw new BackendServiceException(ErrorCode.ROLE_EXISTED);
        Role savedRole = roleRepository.save(Role.builder()
                .name(request.name())
                .description(request.description())
                .build());
        return RoleDetailResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .description(savedRole.getDescription())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDetailResponse> getAllRoleWithPermission() {
        List<Role> roles = roleRepository.findAllRoleWithPermission();
        return roles.stream().map(role -> RoleDetailResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getRoleHasPermissions().stream()
                        .map(RoleHasPermission::getPermission)
                        .map(Permission::getName).toList())
                .build()).toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "role_permissions", allEntries = true)
    public AssignPermissionResponse assignPermission(String roleId, AssignPermissionRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BackendServiceException(ErrorCode.ROLE_NOT_EXISTED));
        List<Permission> permissions = permissionRepository.findAllByNameIn(request.permissions());
        permissions.forEach(role::addPermission);
        Role savedRole = roleRepository.save(role);

        return AssignPermissionResponse.builder()
                .roleId(roleId)
                .roleName(savedRole.getName())
                .permissions(savedRole.getRoleHasPermissions().stream()
                        .map(RoleHasPermission::getPermission)
                        .map(Permission::getName).toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "role_permissions", key = "#roleNames.toString()")
    public List<RoleDetailResponse> getAllRoleWithPermissionByRoleName(List<String> roleNames) {
        return roleRepository.findAllRoleWithPermissionByRoleName(roleNames).stream()
                .map(role -> RoleDetailResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(role.getRoleHasPermissions().stream()
                                .map(RoleHasPermission::getPermission)
                                .map(Permission::getName)
                                .collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());
    }
}
