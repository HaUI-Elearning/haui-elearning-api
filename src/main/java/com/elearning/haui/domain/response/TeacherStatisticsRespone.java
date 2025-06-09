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
   private int TotalCourse;
   private int TotalapprovedCourse;
   private int TotalRejectCourse;
   private int TotalPendingCourse;
   private List<CourseMonthlyGrowthDTO> ListCourseStatistics;
   private List<TeacherCourseDTO> approvedCourse;
   private List<TeacherCourseDTO> RejectCourse;
   private List<TeacherCourseDTO> PendingCourse;
}
