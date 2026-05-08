package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.PermissionCreateRequest;
import com.nhpdev.backendservice.dto.request.PermissionDeletedRequest;
import com.nhpdev.backendservice.dto.response.ApiResponse;
import com.nhpdev.backendservice.dto.response.PermissionDetailResponse;
import com.nhpdev.backendservice.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/permissions")
@Tag(name = "PERMISSION APIs")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PreAuthorize("hasAuthority('PERMISSION:CREATE') or hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<PermissionDetailResponse> createPermission(@RequestBody  PermissionCreateRequest request) {
        return ApiResponse.<PermissionDetailResponse>builder()
                .ok(true)
                .data(permissionService.createPermission(request))
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @PreAuthorize("hasAuthority('PERMISSION:READ') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ApiResponse<List<PermissionDetailResponse>> getAllPermission() {
        return ApiResponse.<List<PermissionDetailResponse>>builder()
                .ok(true)
                .data(permissionService.getAllPermission())
                .code(HttpStatus.OK.value())
                .build();
    }

    @PreAuthorize("hasAuthority('PERMISSION:DELETE') and hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ApiResponse<Object> deletePermissionByName(@RequestBody PermissionDeletedRequest request) {
        permissionService.deletePermission(request);
        return ApiResponse.builder()
                .ok(true)
                .code(HttpStatus.NO_CONTENT.value())
                .build();
    }
}
