package com.nhpdev.backendservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.access_key}")
    private String accessKey;

    @Value("${jwt.refresh_key}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String userId, List<String> authorities) {
        //HEADER
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //BODY
        Date expirationDate = Date.from(new Date().toInstant().plus(30, ChronoUnit.MINUTES));
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issuer("http://locahost:8080")
                .expirationTime(expirationDate)
                .claim("authorities", authorities)
                .build();
        //SIGN
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new MACSigner(accessKey));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRefreshToken(String userId) {
        //HEADER
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //BODY
        Date expirationDate = Date.from(new Date().toInstant().plus(14, ChronoUnit.DAYS));
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issuer("http://locahost:8080")
                .expirationTime(expirationDate)
                .build();
        //SIGN
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new MACSigner(refreshKey));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> extractAuthorities(Object authorities) {
        if(authorities == null)
            return List.of();
        else if(authorities instanceof List<?> list)
            return list.stream().map(String::valueOf).toList();
        return List.of();
    }
}
