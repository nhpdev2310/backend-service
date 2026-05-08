package com.nhpdev.backendservice.dto.request;

import java.util.List;

public record AssignPermissionRequest(
        List<String> permissions
) {
}
