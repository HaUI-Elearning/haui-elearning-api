package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespone {
    private Long userId;
    private String name;
    private String email;
    private String roleName;
    private LocalDateTime createdAt;
    private boolean emailVerified;
}
