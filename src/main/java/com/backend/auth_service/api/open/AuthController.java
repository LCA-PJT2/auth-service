package com.backend.auth_service.api.open;

import com.backend.auth_service.common.dto.ApiResponseDto;
import com.backend.auth_service.common.web.context.GatewayRequestHeaderUtils;
import com.backend.auth_service.dto.ReissueTokenResponseDto;
import com.backend.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/reissue")
    public ResponseEntity<ApiResponseDto<ReissueTokenResponseDto>> reissueToken() {
        Long userId = Long.valueOf(GatewayRequestHeaderUtils.getUserIdOrThrowException());
        ReissueTokenResponseDto token = authService.reissueToken(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(token));

    }
}
