package com.elearning.haui.service.validator;

import com.elearning.haui.domain.dto.RegisterDTO;
import com.elearning.haui.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {

    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(RegisterDTO registerDTO, ConstraintValidatorContext context) {
        boolean isValid = true;

        // Validate username (non-null and minimum 3 characters)
        if (registerDTO.getUsername() == null || registerDTO.getUsername().length() < 3) {
            context.buildConstraintViolationWithTemplate("Username must be at least 3 characters long")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            isValid = false;
        }

        // Validate name (non-null and minimum 2 characters)
        if (registerDTO.getName() == null || registerDTO.getName().length() < 2) {
            context.buildConstraintViolationWithTemplate("Name must be at least 2 characters long")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        // Validate email format
        if (registerDTO.getEmail() == null || !EmailValidator.getInstance().isValid(registerDTO.getEmail())) {
            context.buildConstraintViolationWithTemplate("Invalid email format")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        // Validate password length and matching confirmPassword
        if (registerDTO.getPassword() == null || registerDTO.getPassword().length() < 6) {
            context.buildConstraintViolationWithTemplate("Password must be at least 6 characters long")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        if (this.userService.checkEmailExist(registerDTO.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email already exists")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            isValid = false;
        }

        if (this.userService.checkUsernameExist(registerDTO.getUsername())) {
            context.buildConstraintViolationWithTemplate("Username already exists")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            isValid = false;
        }

        // Only return true if all fields are valid
        return isValid;
    }
}
