package com.backend.auth_service.common.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통 에러
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,      "COMMON400", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND,               "COMMON404", "리소스를 찾을 수 없습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러입니다."),
    
    // S3 관련 에러
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 업로드 실패"),
    
    // 사용자 관련 에러
    USER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "USER_400", "이미 사용 중인 이메일입니다."),
    USER_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "USER_401", "이미 사용 중인 닉네임입니다."),
    USER_DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, "USER_402", "기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."),
    USER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_403", "기존 비밀번호와 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "사용자를 찾을 수 없습니다."),
    
    // 인증 관련 에러
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_401", "잘못된 인증 정보입니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_402", "토큰이 만료되었습니다."),
    AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_403", "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_NOTFOUND(HttpStatus.UNAUTHORIZED, "AUTH_404", "토큰을 찾을 수 없습니다."),
    
    // 헤더 관련 에러
    HEADER_USER_ID_MISSING(HttpStatus.BAD_REQUEST, "HEADER_400", "헤더에 userId 정보가 없습니다."),
    HEADER_DEVICE_MISSING(HttpStatus.BAD_REQUEST, "HEADER_401", "헤더에 사용자 디바이스 정보가 없습니다."),
    HEADER_ADDRESS_MISSING(HttpStatus.BAD_REQUEST, "HEADER_402", "헤더에 사용자 IP 주소 정보가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
