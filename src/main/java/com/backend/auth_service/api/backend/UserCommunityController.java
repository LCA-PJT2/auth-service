package com.backend.auth_service.api.backend;

import com.backend.auth_service.dto.UserInfoDto;
import com.backend.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1/user")
@RequiredArgsConstructor
public class UserCommunityController {

    private final UserService userService;

    @PostMapping("/usernames")
    public Map<String, String> getUsernames(@RequestBody List<String> userIds) {
        return userService.getUsernames(userIds);
    }

    @GetMapping("/username")
    public UserInfoDto.UserResponse getUserInfo(@RequestParam("userId") String userId) {
        return userService.getUserInfo(userId);
    }
}
