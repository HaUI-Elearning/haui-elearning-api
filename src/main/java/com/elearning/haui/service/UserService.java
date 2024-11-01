package com.elearning.haui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elearning.haui.entity.User;
import com.elearning.haui.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public void removeById(long id) {
        this.userRepository.deleteById(id);
    }

}
