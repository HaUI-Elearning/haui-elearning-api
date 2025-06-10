package com.elearning.haui.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 50, message = "Họ và tên phải từ 2-50 ký tự")
    private String name;

    @NotBlank(message = "Giới thiệu không được để trống")
    @Size(min = 5, max = 150, message = "Giới thiệu phải từ 5-150 ký tự")
    private String introduce;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0.*$", message = "Số điện thoại phải bắt đầu bằng 0")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải gồm đúng 10 chữ số")
    private String phoneNumber;
}
