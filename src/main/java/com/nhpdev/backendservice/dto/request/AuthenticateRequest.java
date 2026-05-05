package com.nhpdev.backendservice.dto.request;

import lombok.Builder;

@Builder
public record AuthenticateRequest(
        String email,
        String password
) {
}
