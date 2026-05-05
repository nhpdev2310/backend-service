package com.nhpdev.backendservice.dto.request;

public record UserChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String newPassword2
) {
}
