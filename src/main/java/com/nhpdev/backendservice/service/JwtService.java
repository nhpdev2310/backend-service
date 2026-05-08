package com.nhpdev.backendservice.service;

import java.util.List;

public interface JwtService {
    public String generateAccessToken(String userId, List<String> authorities);
    public String generateRefreshToken(String userId);
    List<String> extractAuthorities(Object authorities);
}
