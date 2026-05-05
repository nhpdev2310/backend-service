package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

@Builder
public record AuthenticateResponse(
        String user_id,
        String accessToken,
        String refreshToken
) {
}
