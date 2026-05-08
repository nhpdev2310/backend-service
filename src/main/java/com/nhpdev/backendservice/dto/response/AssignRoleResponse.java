package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AssignRoleResponse(
        String userId,
        List<String> roles
) {
}
