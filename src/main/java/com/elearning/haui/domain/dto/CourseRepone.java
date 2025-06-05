package com.elearning.haui.domain.dto;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRepone {
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
}
