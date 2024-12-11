package com.elearning.haui.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.UserDetailsDTO;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.request.UpdateUserProfileRequest;
import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.service.RoleService;
import com.elearning.haui.service.UserDetailsService;
import com.elearning.haui.service.UserService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UserAPI {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserAPI(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<User>> register(@RequestBody RegisterDTO registerDTO) throws Exception {
        if (userService.checkEmailExist(registerDTO.getEmail())) {
            throw new Exception("Email already exists.");
        }
        if (userService.checkUsernameExist(registerDTO.getUsername())) {
            throw new Exception("Username already exists.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new Exception("Passwords do not match.");
        }

        User user = userService.registerDTOtoUser(registerDTO);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        user.setRole(this.roleService.findByName("USER"));

        User savedUser = userService.handleSaveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RestResponse<>(
                        HttpStatus.CREATED.value(),
                        null,
                        "User successfully registered",
                        savedUser));
    }

    // Lấy danh sách người dùng, phân trang
    @GetMapping("/users")
    public ResponseEntity<RestResponse<ResultPaginationDTO>> getAllUser(
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO result = this.userService.fetchAllUser(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse<>(HttpStatus.OK.value(), null, "Successfully fetched users", result));
    }

    // lấy người dùng theo id
    @GetMapping("/users/{id}")
    public ResponseEntity<RestResponse<User>> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.getUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new RestResponse<>(
                        HttpStatus.OK.value(),
                        null,
                        "User details retrieved",
                        fetchUser));
    }

    // lấy thông tin của người dùng
    @GetMapping("/users/my-profile")
    public ResponseEntity<UserDetailsDTO> getUser() throws Exception {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        User user = userService.getUserByUsername(username);

        if (user == null) {
            new Exception("User not found");
        }

        return ResponseEntity.ok().body(userService.mapToUserDetailsDTO(user));

    }

    // Cập nhập người dùng
    @PutMapping("/users/update-profile")
    public ResponseEntity<UserDetailsDTO> updateUserProfile(@RequestBody UpdateUserProfileRequest request) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username từ JWT

        // Cập nhật thông tin người dùng
        User updatedUser = userService.updateUserProfile(username, request);

        // Trả về thông tin đã cập nhật dưới dạng UserDetailsDTO
        return ResponseEntity.ok().body(userService.mapToUserDetailsDTO(updatedUser));
    }

    // Xoá người dùng theo id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("User not found");
        }

        userService.handleDeleteUser(id);

        return ResponseEntity.ok(
                new RestResponse<>(
                        HttpStatus.OK.value(),
                        null,
                        "Successfully deleted user",
                        null));
    }

}
