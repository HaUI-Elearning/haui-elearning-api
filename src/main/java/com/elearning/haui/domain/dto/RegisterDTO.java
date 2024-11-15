package com.elearning.haui.domain.dto;

import com.elearning.haui.service.validator.RegisterChecked;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RegisterChecked
public class RegisterDTO {
    private String username;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

}
