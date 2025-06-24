package com.backend.auth_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoDto {

    @Data
    public static class UserRequest{
        private String userId;
    }

    @Data
    @AllArgsConstructor
    public static class UserResponse{
        private String userId;
        private String userName;
    }
}
