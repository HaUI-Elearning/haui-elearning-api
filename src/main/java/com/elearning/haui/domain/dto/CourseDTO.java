package com.elearning.haui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String name;
    private String thumbnail;
    private String description;
    private String contents;
    private float star;
    private int hour;
    private double price;
    private String author;

}
