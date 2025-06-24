package com.backend.auth_service.api.open;

import com.backend.auth_service.common.dto.ApiResponseDto;
import com.backend.auth_service.common.web.context.GatewayRequestHeaderUtils;
import com.backend.auth_service.dto.LoginRequestDto;
import com.backend.auth_service.dto.LoginResponseDto;
import com.backend.auth_service.dto.SignupRequestDto;
import com.backend.auth_service.dto.UpdateUserRequestDto;
import com.backend.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<String>> signup(
            @Valid @RequestPart("info") SignupRequestDto dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile file
    ){
        userService.signup(dto, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("회원가입 성공"));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto response = userService.login(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(response));
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto<String>> updateUser(
            @RequestPart("info")UpdateUserRequestDto dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile file
            ) {
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        userService.updateUser(userId, dto, file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success("정보수정 성공"));
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ApiResponseDto<String>> deleteUser() {
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success("회원 탈퇴 완료"));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<ApiResponseDto<String>> logout() {
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        userService.logout(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success("로그아웃 완료"));
    }
}
