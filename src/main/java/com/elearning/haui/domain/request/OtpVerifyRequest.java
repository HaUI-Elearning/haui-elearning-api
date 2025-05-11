package com.elearning.haui.domain.request;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OtpVerifyRequest {
    private long UserId;
    private String otp;
}
