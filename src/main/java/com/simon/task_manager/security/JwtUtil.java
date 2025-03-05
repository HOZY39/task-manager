package com.simon.task_manager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "superTajnyKluczJWTKtoryJestWystarczajacoDlugi";

    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 godzin
                .signWith(key, Jwts.SIG.HS256) // Nowy sposób podpisywania
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key) // Nowy sposób weryfikacji
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
