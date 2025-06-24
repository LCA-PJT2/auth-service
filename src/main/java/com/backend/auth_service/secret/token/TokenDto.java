package com.backend.auth_service.secret.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtToken {
        private String token;
        private int expiresIn;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessToken {
        private JwtToken accessToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessRefreshToken {
        private JwtToken accessToken;
        private JwtToken refreshToken;
    }
} 