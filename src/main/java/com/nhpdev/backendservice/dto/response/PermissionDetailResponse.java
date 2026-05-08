package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

@Builder
public record PermissionDetailResponse(
        String id,
        String name,
        String description
) {
}
