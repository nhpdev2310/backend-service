package com.nhpdev.backendservice.dto.request;

public record UserCreateRequest(
        String email,
        String username,
        String password
) {
}
