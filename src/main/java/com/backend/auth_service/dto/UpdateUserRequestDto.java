package com.backend.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String newPassword;
}
