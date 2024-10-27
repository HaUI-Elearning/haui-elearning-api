package com.elearning.haui.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.api.UserApi;
import com.elearning.haui.entity.User;
import com.elearning.haui.service.UserService;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> getList() {
        return this.userService.fetchUsers();
    }

    @Override
    public ResponseEntity<User> createNewUser(User user) {
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.ok(newUser);
    }

    @Override
    public ResponseEntity<?> delUserById(long userId) {
        this.userService.handleDeleteUser(userId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<User> getUserById(long userId) {
        return ResponseEntity.ok(this.userService.fetchUserById(userId));
    }

    @Override
    public ResponseEntity<User> updateUser(User reqUser) {
        return ResponseEntity.ok(this.userService.handleUpdateUser(reqUser));
    }
}
