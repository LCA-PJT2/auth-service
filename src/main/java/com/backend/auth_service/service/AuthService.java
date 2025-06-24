package com.backend.auth_service.service;

import com.backend.auth_service.common.code.ErrorCode;
import com.backend.auth_service.common.exception.BusinessException;
import com.backend.auth_service.domain.User;
import com.backend.auth_service.dto.ReissueTokenResponseDto;
import com.backend.auth_service.repository.UserRepository;
import com.backend.auth_service.secret.token.TokenDto;
import com.backend.auth_service.secret.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    public ReissueTokenResponseDto reissueToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String redisKey = REFRESH_TOKEN_PREFIX + user.getEmail();
        String storedRefreshToken = (String) redisTemplate.opsForValue().get(redisKey);

        if (storedRefreshToken == null) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_NOTFOUND);
        }

        String extractedUserId = tokenGenerator.validateJwtToken(storedRefreshToken);
        if (extractedUserId == null || !extractedUserId.equals(user.getId().toString())) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        TokenDto.AccessToken newTokens = tokenGenerator.generateAccessToken(user.getEmail());

        return new ReissueTokenResponseDto(newTokens.getAccessToken().getToken());
    }
}
