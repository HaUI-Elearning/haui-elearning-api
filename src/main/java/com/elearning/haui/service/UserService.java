package com.elearning.haui.service;

import com.elearning.haui.entity.User;
import com.elearning.haui.repository.UserRepository;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }

    public List<User> fetchUsers() {
        return this.userRepository.findAll();
    }

    public User fetchUserById(long userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getUserId());
        if (currentUser != null) {
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassword(reqUser.getPassword());
            this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
