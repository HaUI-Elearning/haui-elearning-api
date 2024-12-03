package com.elearning.haui.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCourseDTO {
    private Long categoryId;
    private String name;
    private List<CourseDTO> courseCategories;
}
