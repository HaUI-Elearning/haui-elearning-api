package com.elearning.haui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.LoginDTO;
import com.elearning.haui.domain.entity.OtpToken;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.response.ResLoginDTO;
import com.elearning.haui.repository.UserRepository;
import com.elearning.haui.service.OtpService;
import com.elearning.haui.utils.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthAPI {

    @Autowired
    OtpService otpService;

    @Autowired 
    UserRepository userrepository;
    private final SecurityUtil securityUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthAPI(SecurityUtil securityUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.securityUtil = securityUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Lấy user đã xác thực
       
        User user = userrepository.findByUsername(authentication.getName());
        if(user.getRole().getId()==1)
        {
            throw new Exception("Can not login with account");
        }
        
        // Kiểm tra xác thực email
        if (!user.isEmailVerified()) {
            otpService.sendOtpEmail(user, "REGISTER");
            throw new Exception("user not verify");
        }

        // create a token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }

    @PostMapping("/api/v1/login/Admin")
    public ResponseEntity<ResLoginDTO> loginAdmin(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Lấy user đã xác thực
       
        User user = userrepository.findByUsername(authentication.getName());
        if(!(user.getRole().getId()==1))
        {
            throw new Exception("Can not login with account");
        }
        
        // Kiểm tra xác thực email
        if (!user.isEmailVerified()) {
            otpService.sendOtpEmail(user, "REGISTER");
            throw new Exception("user not verify");
        }

        // create a token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
