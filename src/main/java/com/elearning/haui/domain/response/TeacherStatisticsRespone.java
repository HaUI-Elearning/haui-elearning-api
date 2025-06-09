package com.elearning.haui.domain.response;
import java.util.List;

import com.elearning.haui.domain.dto.CourseMonthlyGrowthDTO;
import com.elearning.haui.domain.dto.TeacherCourseDTO;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherStatisticsRespone {
    List<CourseMonthlyGrowthDTO> ListCourseStatistics;
    List<TeacherCourseDTO> approvedCourse;
    List<TeacherCourseDTO> RejectCourse;
    List<TeacherCourseDTO> PendingCourse;
}
