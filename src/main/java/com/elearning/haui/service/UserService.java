package com.elearning.haui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.Role;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.request.UpdateUserProfileRequest;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseRepone;
import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.dto.Meta;
import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.TeacherCourseDTO;
import com.elearning.haui.domain.dto.TeacherDTO;
import com.elearning.haui.domain.dto.UserDTO;
import com.elearning.haui.domain.dto.UserDetailsDTO;
import com.elearning.haui.domain.dto.UserRespone;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
import com.elearning.haui.repository.RoleRepository;
import com.elearning.haui.repository.UserRepository;
import com.elearning.haui.utils.SecurityUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    CourseRepository courseRepository;
   
    @Autowired
    private SecurityUtil securityUtil;


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

    public CourseRepone mapToCourseDTO(Course course) {
    if (course == null) {
        return null;
    }
    return new CourseRepone(
        course.getCourseId(),
        course.getName(),
        course.getThumbnail(),
        course.getDescription(),
        course.getContents(),
        course.getStar(),
        course.getHour(),
        course.getPrice(),
        course.getSold(),
        course.getAuthor() != null ? course.getAuthor().getName() : null,
        course.getCreatedAt(),
        course.getApprovalStatus()
    );
}
        public Object mapUserDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByUser_UserId(user.getUserId());
        List<CourseRepone> enrolledCourses = enrollments != null ? enrollments.stream()
            .map(enrollment -> mapToCourseDTO(enrollment.getCourse()))
            .collect(Collectors.toList()) : new ArrayList<>();
        if (user.getRole().getId() == 2) {
            return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().getName(),
                user.getCreatedAt(),
                user.isEmailVerified(),
                enrolledCourses
            );
        } else if (user.getRole().getId() == 3) { 
            
            List<Course> created = courseRepository.getAllCourseByTeacher(user.getUsername());
            
            List<CourseRepone> createdCourses = created != null ? created.stream()
                .map(course -> mapToCourseDTO(course))
                .collect(Collectors.toList()) : new ArrayList<>();
            return new TeacherDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().getName(),
                user.getCreatedAt(),
                user.isEmailVerified(),
                enrolledCourses,
                createdCourses
            );
        } else {
            return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().getName(),
                user.getCreatedAt(),
                user.isEmailVerified(),
                new ArrayList<>()
            );
        }
    }
    public Object getUserById(Long UserId)
    {
        User user=userRepository.findById(UserId).orElseThrow(()-> new RuntimeException("User not found"));
        
        return mapUserDTO(user);
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

    public ResultPaginationDTO fetchAllUserSummaries(Pageable pageable) {
        
        Page<UserRespone> userPage = userRepository.findUserSummaries(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(userPage.getNumber() + 1);
        mt.setPageSize(userPage.getSize());
        mt.setPages(userPage.getTotalPages());
        mt.setTotal(userPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(userPage.getContent()); 

        return rs;
    }

    public ResultPaginationDTO fetchAllUserByRole(Pageable pageable,Long RoleId) {
        
        Page<UserRespone> userPage = userRepository.findUsersByRole(pageable,RoleId);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(userPage.getNumber() + 1);
        mt.setPageSize(userPage.getSize());
        mt.setPages(userPage.getTotalPages());
        mt.setTotal(userPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(userPage.getContent()); 

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
    //find user by mail
    public User findUserByEmail(String email){
        User user=userRepository.findUserByEmail(email);
        return user;
    }

    //find user by mail
    public User findUserByName(String name){
        User user=userRepository.findByName(name);
        return user;
    }

    //Register Teacher
     public String registerTeacher(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found for username: " + username);
        }

        Role role = roleRepository.findByName("TEACHER");
        if (role == null) {
            role = new Role();
            role.setName("TEACHER");
            role.setDescription("Giáo viên");
            role = roleRepository.save(role);
        }

        user.setRole(role);
        userRepository.save(user);
        //  Tạo Authentication mới có thông tin role mới
        List<GrantedAuthority> updatedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));

       Authentication newAuth = new UsernamePasswordAuthenticationToken(
            user.getUsername(), 
            null,
            updatedAuthorities
        );

        // Cập nhật lại Authentication trong context
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        String newToken = securityUtil.createToken(newAuth);

        return newToken;
    }
}
