package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.haui.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    User findById(long id);

    Void deleteById(long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmailAndUserIdNot(String username, String email, Long userId);
}
