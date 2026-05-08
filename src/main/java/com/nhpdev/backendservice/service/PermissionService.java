package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.PermissionCreateRequest;
import com.nhpdev.backendservice.dto.request.PermissionDeletedRequest;
import com.nhpdev.backendservice.dto.response.PermissionDetailResponse;

import java.util.List;

public interface PermissionService {
    PermissionDetailResponse createPermission(PermissionCreateRequest request);
    List<PermissionDetailResponse> getAllPermission();
    void deletePermission(PermissionDeletedRequest request);
}
