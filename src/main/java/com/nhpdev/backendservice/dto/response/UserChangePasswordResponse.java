package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

@Builder
public record UserChangePasswordResponse(
        String id,
        String username
) {
}
