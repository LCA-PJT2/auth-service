package com.backend.auth_service.common.dto;

import com.backend.auth_service.common.code.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto<T> {

    private boolean isSuccess;
    private String code;
    private String message;
    private T result;

    public static <T> ApiResponseDto<T> success(T data) {
        ApiResponseDto<T> r = new ApiResponseDto<>();
        r.isSuccess = true;
        r.code = "COMMON200";
        r.message = "성공입니다.";
        r.result = data;
        return r;
    }

    public static <T> ApiResponseDto<T> createError(String code, String message) {
        ApiResponseDto<T> r = new ApiResponseDto<>();
        r.isSuccess = false;
        r.code = code;
        r.message = message;
        return r;
    }

    public static <T> ApiResponseDto<T> createError(String code, String message, T data) {
        ApiResponseDto<T> r = createError(code, message);
        r.result = data;
        return r;
    }

    public static <T> ApiResponseDto<T> createError(ErrorCode ec) {
        ApiResponseDto<T> r = new ApiResponseDto<>();
        r.isSuccess = false;
        r.code = ec.getCode();
        r.message = ec.getMessage();
        return r;
    }

    public static <T> ApiResponseDto<T> createError(ErrorCode ec, T data) {
        ApiResponseDto<T> r = createError(ec);
        r.result = data;
        return r;
    }
}
