package com.elearning.haui.controller.Admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/authentication";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerUser", registerDTO);
            return "client/auth/authentication";
        }

        registerDTO.setPassword(this.passwordEncoder.encode(registerDTO.getPassword()));
        User user = this.userService.registerDTOtoUser(registerDTO);
        this.userService.handleSaveUser(user);

        return "client/auth/authentication";
    }

}
