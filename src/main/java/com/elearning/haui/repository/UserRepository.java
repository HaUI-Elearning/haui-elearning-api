package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.haui.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    User findById(long id);

    Void deleteById(long id);
}
