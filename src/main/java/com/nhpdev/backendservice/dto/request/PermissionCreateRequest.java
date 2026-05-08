package com.nhpdev.backendservice.dto.request;

public record PermissionCreateRequest(
        String name,
        String description
) {
}
