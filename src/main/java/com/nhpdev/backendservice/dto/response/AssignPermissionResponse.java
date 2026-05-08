package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AssignPermissionResponse(
        String roleId,
        String roleName,
        List<String> permissions
) {
}
