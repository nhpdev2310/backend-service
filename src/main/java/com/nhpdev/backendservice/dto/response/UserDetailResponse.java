package com.nhpdev.backendservice.dto.response;

import lombok.Builder;

@Builder
public record UserDetailResponse (
        String id,
        String email,
        String username,
        String status
){
}
