package com.elearning.haui.domain.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCourseDTO {
    private Long courseId;
    private String name;
    private String thumbnail;
    private String description;
    private String contents;
    private Double star;
    private Double hour;
    private double price;
    private int sold;
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long CategoryId;
    private String approvalStatus;

}
