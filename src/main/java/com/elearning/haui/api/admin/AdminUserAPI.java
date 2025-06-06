 package com.elearning.haui.api.admin;

import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.service.RoleService;
import com.elearning.haui.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/Admin")
public class AdminUserAPI {

    private final UserService userService;
    private final RoleService roleService;

    public AdminUserAPI(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    // Create User
    @PostMapping("/users")
    public  ResponseEntity<?> createUser(@RequestBody RegisterDTO registerDTO) throws Exception {
        if (userService.checkEmailExist(registerDTO.getEmail())) {
            throw new Exception("Email already exists.");
        }
        if (userService.checkUsernameExist(registerDTO.getUsername())) {
            throw new Exception("Username already exists.");
        }

        User user = userService.registerDTOtoUser(registerDTO);
        user.setRole(roleService.findByName(registerDTO.getRole()));
        user.setIntroduce(registerDTO.getIntroduce());
        user.setEmailVerified(true);
        User savedUser = userService.handleSaveUser(user);


        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RestResponse<>(
                        HttpStatus.CREATED.value(),
                        null,
                        "User registered successfully.",
                        savedUser));
    }

    // Padding User
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

    // Get User by Id
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

    //Update user
    @PutMapping("")
    public ResponseEntity<RestResponse<User>> updateUser(@RequestBody RegisterDTO registerDTO) throws IdInvalidException {
        return null;
    }
}
