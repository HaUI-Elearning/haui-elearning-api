package com.elearning.haui.domain.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRevenueDTO {
    private Long courseId;
    private String courseName;
    private Double price;
    private Double star;
    private Double revenue;
    private int Sold;
}
