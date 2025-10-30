package com.shyn9yskhan.authentication_service.service;

import com.shyn9yskhan.authentication_service.service.util.KeyUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final long expirationMs;
    private final String keyId;

    public JwtService(@Value("${jwt.private-key-path}") String privateKeyPath,
                      @Value("${jwt.public-key-path}") String publicKeyPath,
                      @Value("${jwt.expiration-ms}") long expirationMs,
                      @Value("${jwt.keyId}") String keyId) throws Exception {
        this.privateKey = KeyUtil.loadPrivateKey(Path.of(privateKeyPath));
        this.publicKey = KeyUtil.loadPublicKey(Path.of(publicKeyPath));
        this.expirationMs = expirationMs;
        this.keyId = keyId;
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .header()
                .keyId(keyId)
                .and()
                .signWith(privateKey)
                .compact();
    }

    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);
    }

    public void validateToken(String token) throws JwtException {
        parseToken(token);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public String getKeyId() {
        return keyId;
    }
}
