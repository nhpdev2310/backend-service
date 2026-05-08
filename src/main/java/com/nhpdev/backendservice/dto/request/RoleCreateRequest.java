package com.nhpdev.backendservice.dto.request;

import lombok.Builder;

@Builder
public record RoleCreateRequest(
        String name,
        String description
) {
}
