package com.backend.auth_service.advice;

import com.backend.auth_service.common.code.ErrorCode;
import com.backend.auth_service.common.dto.ApiResponseDto;
import com.backend.auth_service.common.dto.ValidationErrorDto;
import com.backend.auth_service.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBusinessException(BusinessException ex) {
        log.warn("BusinessException: {}", ex.getMessage());
        
        ApiResponseDto<Void> response = ApiResponseDto.createError(ex.getErrorCode());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(response);
    }

    /**
     * 파라미터 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<ValidationErrorDto>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("ValidationException: {}", ex.getMessage());
        
        BindingResult bindingResult = ex.getBindingResult();
        ValidationErrorDto validationError = ValidationErrorDto.of(bindingResult);
        
        ApiResponseDto<ValidationErrorDto> response = ApiResponseDto.createError(ErrorCode.INVALID_PARAMETER, validationError);
        return ResponseEntity
                .status(ErrorCode.INVALID_PARAMETER.getStatus())
                .body(response);
    }

    /**
     * 리소스 없음 예외 처리
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("NoResourceFoundException: {}", ex.getMessage());
        
        ApiResponseDto<Void> response = ApiResponseDto.createError(ErrorCode.NOT_FOUND);
        return ResponseEntity
                .status(ErrorCode.NOT_FOUND.getStatus())
                .body(response);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        
        ApiResponseDto<Void> response = ApiResponseDto.createError(ErrorCode.SERVER_ERROR);
        return ResponseEntity
                .status(ErrorCode.SERVER_ERROR.getStatus())
                .body(response);
    }
} 