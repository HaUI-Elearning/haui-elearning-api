package com.elearning.haui.domain.dto;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseMonthlyGrowthDTO {
    private Long courseId;
    private String courseName;
    private int month;
    private int year;
    private int newStudents;
    private int prevMonthStudents;
    private int growth;
    private boolean Warning;
}
