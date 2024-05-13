package com.openclassroom.chatopjava.service;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;


    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
        String secret = "CyvAycrHQfjQV6bBkS7Vg3yACEmcUcC9aHB7WzH0ngU5wEdU6BH4Bv22KA4uDGge";
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    public String generateToken(Authentication authentication) {
        System.out.println(authentication);
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:4200")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getPrincipal().toString())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
    public void validateToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                Jwt jwt = jwtDecoder.decode(token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
    }
    public String getSubjectFromToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                Jwt jwt = jwtDecoder.decode(token);
                return (String) jwt.getClaims().get("sub");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
    }
}
