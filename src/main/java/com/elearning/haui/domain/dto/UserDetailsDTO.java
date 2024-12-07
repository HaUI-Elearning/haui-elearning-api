package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String introduce;
    private List<FavoriteCourseDTO> favoriteCourses;
    private LocalDateTime createdAt;
}
