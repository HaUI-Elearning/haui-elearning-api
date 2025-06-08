package com.elearning.haui.domain.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRevenueDTO {
    private Long courseId;
    private String courseName;
    private Double revenue;
    private int Sold;
}
