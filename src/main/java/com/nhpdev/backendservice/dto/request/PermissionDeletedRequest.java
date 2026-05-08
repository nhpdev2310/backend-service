package com.nhpdev.backendservice.dto.request;

import lombok.Builder;

@Builder
public record PermissionDeletedRequest(
        String permissionName
) {
}
