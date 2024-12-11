package com.elearning.haui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.Role;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.request.UpdateUserProfileRequest;
import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.dto.Meta;
import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.UserDetailsDTO;
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

    public void handleDeleteUser(long id) {
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

    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
    }

    public boolean isUsernameOrEmailExist(String username, String email, Long userId) {
        if (userId == null) {
            return userRepository.existsByUsernameOrEmail(username, email);
        } else {
            return userRepository.existsByUsernameOrEmailAndUserIdNot(username, email, userId);
        }
    }

    public UserDetailsDTO mapToUserDetailsDTO(User user) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(user.getUserId());
        userDetailsDTO.setUsername(user.getUsername());
        userDetailsDTO.setEmail(user.getEmail());
        userDetailsDTO.setName(user.getName());
        userDetailsDTO.setIntroduce(user.getIntroduce());
        userDetailsDTO.setCreatedAt(user.getCreatedAt());

        userDetailsDTO.setFavoriteCourses(
                user.getFavoriteCourses().stream()
                        .map(this::mapToFavoriteCourseDTO)
                        .collect(Collectors.toList()));

        return userDetailsDTO;
    }

    private FavoriteCourseDTO mapToFavoriteCourseDTO(FavoriteCourse favoriteCourse) {
        FavoriteCourseDTO favoriteCourseDTO = new FavoriteCourseDTO();
        favoriteCourseDTO.setId(favoriteCourse.getId());
        favoriteCourseDTO.setAddedAt(favoriteCourse.getAddedAt());
        return favoriteCourseDTO;
    }

    public User updateUserProfile(String username, UpdateUserProfileRequest updateUserProfileRequest) {
        // Tìm user từ username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found for username: " + username);
        }

        // Cập nhật nếu trường không null
        if (updateUserProfileRequest.getName() != null && !updateUserProfileRequest.getName().isBlank()) {
            user.setName(updateUserProfileRequest.getName());
        }

        if (updateUserProfileRequest.getIntroduce() != null && !updateUserProfileRequest.getIntroduce().isBlank()) {
            user.setIntroduce(updateUserProfileRequest.getIntroduce());
        }

        // Lưu thông tin đã cập nhật
        userRepository.save(user);

        return user;
    }
}
