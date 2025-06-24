package com.backend.auth_service.service;

import com.backend.auth_service.common.code.ErrorCode;
import com.backend.auth_service.common.exception.BusinessException;
import com.backend.auth_service.domain.User;
import com.backend.auth_service.dto.*;
import com.backend.auth_service.feign.CommunityClient;
import com.backend.auth_service.feign.QnaClient;
import com.backend.auth_service.repository.UserRepository;
import com.backend.auth_service.secret.token.TokenDto;
import com.backend.auth_service.secret.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CommunityClient communityClient;
    private final QnaClient qnaClient;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    // 회원가입
    public void signup(SignupRequestDto dto, MultipartFile file) {
        validateDuplicate(dto.getEmail(), dto.getNickname());
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        String imageUrl = extractImageUrl(file);

        User user = User.createUser(
                dto.getEmail(),
                passwordHash,
                dto.getName(),
                dto.getNickname(),
                imageUrl
        );

        userRepository.save(user);
    }

    private void validateDuplicate(String email, String nickname) {
        if(userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE_EMAIL);
        }
        if(userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE_NICKNAME);
        }
    }

    private String extractImageUrl(MultipartFile file) {
        if(file != null && !file.isEmpty()) {
            return s3Service.uploadFile(file);
        }

        return null;
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        validatePassword(dto.getPassword(), user.getPassword());
        TokenDto.AccessRefreshToken tokens = tokenGenerator.generateAccessRefreshToken(user.getEmail());
        storeRefreshToken(user.getEmail(), tokens);

        return toLoginResponseDto(user, tokens);
    }

    private void validatePassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }
    }

    private void storeRefreshToken(String email, TokenDto.AccessRefreshToken tokens) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + email,
                tokens.getRefreshToken().getToken(),
                Duration.ofSeconds(tokens.getRefreshToken().getExpiresIn())
        );
    }

    private LoginResponseDto toLoginResponseDto(User user, TokenDto.AccessRefreshToken tokens) {
        return new LoginResponseDto(
                tokens.getAccessToken().getToken(),
                tokens.getRefreshToken().getToken(),
                tokens.getRefreshToken().getExpiresIn(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }

    // 유저 정보 수정
    public void updateUser(String userId, UpdateUserRequestDto dto, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        updateNickname(user, dto.getNickname());
        updatePassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        String imageUrl = extractImageUrl(file);
        user.updateProfileImageUrl(imageUrl);

        userRepository.save(user);
    }

    private void updateNickname(User user, String nickname) {
        if(nickname == null || nickname.equals(user.getNickname())) return;
        if(userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE_NICKNAME);
        }
        user.changeNickname(nickname);
    }

    private void updatePassword(User user, String currentPassword, String newPassword) {
        if(currentPassword == null || newPassword == null) return;
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_INVALID_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_DUPLICATE_PASSWORD);
        }
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        log.info("회원 탈퇴 시작: {}", user.getEmail());

        redisTemplate.delete(REFRESH_TOKEN_PREFIX + user.getEmail());

        communityClient.deleteCommunityDataByUser(userId);
        qnaClient.deleteQnaDataByUser(userId);

        userRepository.delete(user);
    }

    public void logout(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        redisTemplate.delete(REFRESH_TOKEN_PREFIX + user.getEmail());
    }

    // feign - c > u : 유저 묶음 조회
    public Map<String, String> getUsernames(List<String> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .collect(Collectors.toMap(
                        user -> user.getId().toString(),
                        User::getName
                ));
    }

    // feign - c > u : 유저 단건 조회
    public UserInfoDto.UserResponse getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserInfoDto.UserResponse(
                user.getId().toString(),
                user.getName()
        );
    }
}
