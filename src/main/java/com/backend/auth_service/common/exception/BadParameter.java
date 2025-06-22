package com.backend.auth_service.common.exception;

public class BadParameter extends ClientError{
    public BadParameter(String errorMessage) {
        this.errorCode = "BadParameter";
        this.errorMessage = errorMessage;
    }
}