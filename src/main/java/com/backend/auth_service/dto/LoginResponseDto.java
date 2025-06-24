package com.backend.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final String email;
    private final String name;
    private final String nickname;
    private final String profileImgUrl;
}
