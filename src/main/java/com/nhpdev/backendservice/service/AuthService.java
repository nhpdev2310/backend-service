package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.AuthenticateRequest;
import com.nhpdev.backendservice.dto.response.AuthenticateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface AuthService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
}
