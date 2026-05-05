package com.nhpdev.backendservice.service;

import com.nimbusds.jwt.SignedJWT;

public interface JwtService {
    public String generateAccessToken(String userId);
    public String generateRefreshToken(String userId);
}
