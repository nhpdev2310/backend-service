package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.AssignPermissionRequest;
import com.nhpdev.backendservice.dto.request.RoleCreateRequest;
import com.nhpdev.backendservice.dto.response.ApiResponse;
import com.nhpdev.backendservice.dto.response.AssignPermissionResponse;
import com.nhpdev.backendservice.dto.response.RoleDetailResponse;
import com.nhpdev.backendservice.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@Tag(name = "ROLE APIs")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @PreAuthorize("hasAuthority('ROLE:CREATE') or hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<RoleDetailResponse> createRole(@RequestBody RoleCreateRequest request) {
        return ApiResponse.<RoleDetailResponse>builder()
                .ok(true)
                .data(roleService.createRole(request))
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE:READ') or hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<RoleDetailResponse>> getAllRole() {
        return ApiResponse.<List<RoleDetailResponse>>builder()
                .ok(true)
                .data(roleService.getAllRoleWithPermission())
                .code(HttpStatus.OK.value())
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE:ASSIGN') or hasRole('ADMIN')")
    @PutMapping("/{id}/permissions")
    public ApiResponse<AssignPermissionResponse> assignPermission(@PathVariable(name = "id") String roleId,
                                                                  @RequestBody AssignPermissionRequest request) {
        return ApiResponse.<AssignPermissionResponse>builder()
                .ok(true)
                .data(roleService.assignPermission(roleId, request))
                .code(HttpStatus.OK.value())
                .build();
    }
}
