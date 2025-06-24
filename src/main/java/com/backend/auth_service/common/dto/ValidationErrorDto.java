package com.backend.auth_service.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationErrorDto {
    private List<ValidationFieldError> errors;

    public static ValidationErrorDto of(BindingResult bindingResult) {
        ValidationErrorDto dto = new ValidationErrorDto();
        dto.errors = bindingResult.getFieldErrors().stream()
                .map(ValidationFieldError::of)
                .collect(Collectors.toList());
        return dto;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ValidationFieldError {
        private String field;
        private Object rejectedValue;
        private String message;

        public static ValidationFieldError of(FieldError error) {
            ValidationFieldError fieldError = new ValidationFieldError();
            fieldError.field = error.getField();
            fieldError.rejectedValue = error.getRejectedValue();
            fieldError.message = error.getDefaultMessage();
            return fieldError;
        }
    }
} 