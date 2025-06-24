package com.backend.auth_service.secret.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtConfigProperties configProperties;
    private volatile SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configProperties.getSecretKey()));
                }
            }
        }
        return secretKey;
    }

    public TokenDto.AccessToken generateAccessToken(String userId) {
        TokenDto.JwtToken jwtToken = this.generateJwtToken(userId, false);
        return new TokenDto.AccessToken(jwtToken);
    }

    public TokenDto.AccessRefreshToken generateAccessRefreshToken(String userId) {
        TokenDto.JwtToken accessJwtToken = this.generateJwtToken(userId, false);
        TokenDto.JwtToken refreshJwtToken = this.generateJwtToken(userId, true);
        return new TokenDto.AccessRefreshToken(accessJwtToken, refreshJwtToken);
    }

    public TokenDto.JwtToken generateJwtToken(String userId, boolean refreshToken) {
        int tokenExpiresIn = refreshToken ? configProperties.getRefreshExpiresIn() : configProperties.getAccessExpiresIn();
        String tokenType = refreshToken ? "refresh" : "access";
        String token = Jwts.builder()
                .issuer("Csrrr")
                .subject(userId)
                .claim("userId", userId)
                .claim("tokenType", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiresIn * 1000L))
                .signWith(getSecretKey())
                .header().add("typ", "JWT")
                .and()
                .compact();
        return new TokenDto.JwtToken(token, tokenExpiresIn);
    }

    public String validateJwtToken(String refreshToken) {
        String userId = null;
        final Claims claims = this.verifyAndGetClaims(refreshToken);

        if (claims == null) {
            return null;
        }

        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            return null;
        }

        userId = claims.get("userId", String.class);
        String tokenType = claims.get("tokenType", String.class);

        if (!"refresh".equals(tokenType)) {
            return null;
        }

        return userId;
    }

    private Claims verifyAndGetClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
