package com.elearning.haui.domain.response;

import java.util.List;

import com.elearning.haui.domain.dto.CourseRevenueDTO;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalRevenueTeacherRespone {
    private Double TotalRevennue;
    List<CourseRevenueDTO> Courses;
}
