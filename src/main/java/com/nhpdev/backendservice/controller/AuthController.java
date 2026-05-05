package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.AuthenticateRequest;
import com.nhpdev.backendservice.dto.response.ApiResponse;
import com.nhpdev.backendservice.dto.response.AuthenticateResponse;
import com.nhpdev.backendservice.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Tag(name = "LOGIN")
    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ApiResponse.<AuthenticateResponse>builder()
                .ok(true)
                .data(authService.authenticate(request))
                .message("Login successfully!")
                .code(HttpStatus.OK.value())
                .build();
    }

}
