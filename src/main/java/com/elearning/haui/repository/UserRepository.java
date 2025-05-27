package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elearning.haui.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    @Query("select u from User u where u.email=:email")
    User findUserByEmail(@Param("email") String email);
    User findById(long id);

    Void deleteById(long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByName(String name);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmailAndUserIdNot(String username, String email, Long userId);
}
