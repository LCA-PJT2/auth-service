package com.backend.auth_service.repository;

import com.backend.auth_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);

    List<User> findByIdIn(Collection<Long> userIds);
}
