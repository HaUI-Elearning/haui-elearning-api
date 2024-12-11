package com.elearning.haui.domain.request;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String introduce;
}
