package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.PermissionCreateRequest;
import com.nhpdev.backendservice.dto.request.PermissionDeletedRequest;
import com.nhpdev.backendservice.dto.response.PermissionDetailResponse;
import com.nhpdev.backendservice.entity.Permission;
import com.nhpdev.backendservice.exception.BackendServiceException;
import com.nhpdev.backendservice.exception.ErrorCode;
import com.nhpdev.backendservice.repository.PermissionRepository;
import com.nhpdev.backendservice.repository.RoleHasPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService{
    private final PermissionRepository permissionRepository;
    private final RoleHasPermissionRepository roleHasPermissionRepository;
    @Override
    public PermissionDetailResponse createPermission(PermissionCreateRequest request) {
        if(permissionRepository.existsByName(request.name()))
            throw new BackendServiceException(ErrorCode.PERMISSION_EXISTED);
        Permission permission = Permission.builder()
                .name(request.name())
                .description(request.description())
                .build();
        Permission savedPermission = permissionRepository.save(permission);
        return PermissionDetailResponse.builder()
                .id(savedPermission.getId())
                .name(savedPermission.getName())
                .description(savedPermission.getDescription())
                .build();
    }

    @Override
    public List<PermissionDetailResponse> getAllPermission() {
        return permissionRepository.findAll().stream()
                .map(permission -> PermissionDetailResponse.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .build())
                .toList();
    }

    @Override
    public void deletePermission(PermissionDeletedRequest request) {
        if(!permissionRepository.existsByName(request.permissionName()))
            throw new BackendServiceException(ErrorCode.PERMISSION_NOT_EXISTED);
        Permission permission = permissionRepository.findPermissionByName(request.permissionName());
        roleHasPermissionRepository.deleteByPermissionId(permission.getId());
    }
}
