package com.elearning.haui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.elearning.haui.dto.RegisterDTO;
import com.elearning.haui.entity.Course;
import com.elearning.haui.entity.Enrollment;
import com.elearning.haui.entity.Role;
import com.elearning.haui.entity.User;
import com.elearning.haui.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;

    public UserService(UserRepository userRepository, EnrollmentService enrollmentService) {
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
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

    public Page<User> findPaginated(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());
        Role role = new Role();
        role.setId(1L);
        user.setRole(role);
        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean checkUsernameExist(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<Course> getCourseEnrollment(Long userId) {
        List<Course> results = new ArrayList<>();
        List<Enrollment> enrollment = this.enrollmentService.getEnrollmentByUserId(userId);
        if (enrollment.size() == 0) {
            return null;
        }
        for (Enrollment e : enrollment) {
            results.add(e.getCourse());
        }
        return results;
    }

    // Đếm số lượng user
    public long countUsers() {
        return userRepository.count();
    }
}
