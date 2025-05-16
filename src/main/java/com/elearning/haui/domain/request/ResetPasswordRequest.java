package com.elearning.haui.domain.request;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private String otp;
    private String password;
    private String confirmPassword;
}
