package com.backend.auth_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImageUrl;

    // 연관관계 메서드
    public static User createUser(String email, String password, String name, String nickname, String profileImageUrl) {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }

    public void updateProfileImageUrl(String newProfileImageUrl) {
        if(newProfileImageUrl!=null && !newProfileImageUrl.isEmpty()) {
            this.profileImageUrl = newProfileImageUrl;
        }
    }
}
