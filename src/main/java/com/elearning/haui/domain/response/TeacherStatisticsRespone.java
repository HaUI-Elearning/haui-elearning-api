package com.elearning.haui.domain.response;
import java.util.List;

import com.elearning.haui.domain.dto.CourseMonthlyGrowthDTO;
import com.elearning.haui.domain.dto.CourseRepone;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherStatisticsRespone {
    List<CourseMonthlyGrowthDTO> ListCourseStatistics;
    List<CourseRepone> RejectCourse;
    List<CourseRepone> PendingCourse;
}
