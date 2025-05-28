package com.elearning.haui.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.elearning.haui.domain.entity.OtpToken;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.request.OtpVerifyRequest;
import com.elearning.haui.domain.request.ResetPasswordRequest;
import com.elearning.haui.domain.request.UpdateUserProfileRequest;
import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.repository.OtpTokenRepository;
import com.elearning.haui.service.OtpService;
import com.elearning.haui.service.RoleService;
import com.elearning.haui.service.UserDetailsService;
import com.elearning.haui.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UserAPI {
    @Autowired
    OtpService otpService;
    @Autowired
    OtpTokenRepository otpTokenRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserAPI(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<User>> register(@Valid @RequestBody RegisterDTO registerDTO) throws Exception {
        if (userService.checkEmailExist(registerDTO.getEmail())) {
            throw new Exception("Email already exists.");
        }
        if (userService.checkUsernameExist(registerDTO.getUsername())) {
            throw new Exception("Username already exists.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new Exception("Passwords do not match.");
        }
        if (registerDTO.getUsername().matches("^\\d+$")) {
            throw new IllegalArgumentException("Username cannot be all numbers");
        }
        User user = userService.registerDTOtoUser(registerDTO);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRole(this.roleService.findByName("USER"));
        user.setEmailVerified(false); 
        User savedUser = userService.handleSaveUser(user);

        // Gửi OTP xác thực email
        otpService.sendOtpEmail(savedUser, "REGISTER");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RestResponse<>(
                        HttpStatus.CREATED.value(),
                        null,
                        "User registered successfully. Please verify OTP sent to your email.",
                        savedUser));
    }
    // Verify-register-otp
    @PostMapping("/verify-register-otp")
    public ResponseEntity<?> verifyRegisterOtp(@RequestBody OtpVerifyRequest request) {
        OtpToken token = otpTokenRepository.findValidOtp( request.getOtp(), "REGISTER",LocalDateTime.now());

        if (token != null && token.getExpiresAt().isAfter(LocalDateTime.now())) {
            token.setVerified(true);
            otpTokenRepository.save(token);

            // cập nhật user đã xác thực email
            User user = token.getUser();
            user.setEmailVerified(true);
            userService.handleSaveUser(user);

            return ResponseEntity.ok("Email verified successfully.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP is incorrect or expired.");
    }
    //Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        User user = userService.findUserByEmail(email);
        //System.out.println(user.getEmail());
        //System.out.println(user.isEmailVerified());
        if (user == null || user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Account does not exist or is already verified.");
        }

        otpService.sendOtpEmail(user, "REGISTER");

        return ResponseEntity.ok("New OTP has been sent.");
    }
    // Forgot password
   @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws Exception {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new Exception("Email does not exist..");
        }
        if(user.isEmailVerified()){
             otpService.sendOtpEmail(user, "FORGOT_PASSWORD");
        }
        else{
            throw new RuntimeException("User not verify");
        }
       
        return ResponseEntity.ok("Send OTP forgot password success");
    }
    //Verify-forgot-password
    @PostMapping("/verify-forgot-password-otp")
    public ResponseEntity<?> verifyForgotPasswordOtp(@RequestBody OtpVerifyRequest request) {
        OtpToken token = otpTokenRepository.findValidOtp( request.getOtp(), "FORGOT_PASSWORD",LocalDateTime.now());

        if (token != null
                && token.getExpiresAt().isAfter(LocalDateTime.now())
                && !token.isVerified()
                && !token.isUsed()) {

            token.setVerified(true);
            otpTokenRepository.save(token);

            return ResponseEntity.ok("OTP authentication successful. Proceed to reset password.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP is incorrect, expired, or already used.");
    }

    //Reset pass
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
       
        OtpToken token = otpTokenRepository.findValidOtp(request.getOtp(), "FORGOT_PASSWORD",LocalDateTime.now());

        if (token == null) {
            throw new Exception("OTP is not authenticated or has expired.");
        }

        if (!token.isVerified() || token.isUsed()) {
            throw new Exception("OTP is not verified or has already been used.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new Exception("Confirmation password does not match.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.handleSaveUser(user);

        // set OTP đã được dùng
        token.setUsed(true);
        otpTokenRepository.save(token);

        return ResponseEntity.ok("Password was reset successfully.");
    }
    //resend mail forgot password
    @PostMapping("/resend-forgot-password-otp")
    public ResponseEntity<?> resendForgotPasswordOtp(@RequestParam String email) {
        User user = userService.findUserByEmail(email);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email does not exist.");
        }
        
        // check Account not verified
        if (user.isEmailVerified()) {
            otpService.sendOtpEmail(user, "FORGOT_PASSWORD");
            return ResponseEntity.ok("New OTP has been sent.");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not verified. Please verify your email first.");
    }

    //Register Teacher
    @Operation(summary = "Đăng kí thành giáo viên Respone trả ra token mới của giáo viên,fe xóa token cũ ,lưu token mới vào localstorage")
    @PostMapping("/Register/Teacher")
    public ResponseEntity<?> RegisterTeacher(Authentication authentication){
        String result=userService.registerTeacher(authentication.getName());
        return ResponseEntity.ok(result);
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
