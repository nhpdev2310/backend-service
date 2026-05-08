package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.AuthenticateRequest;
import com.nhpdev.backendservice.dto.response.AuthenticateResponse;

public interface AuthService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
}
