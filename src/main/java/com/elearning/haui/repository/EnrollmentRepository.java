package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Enrollment;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  List<Enrollment> findByUser_UserId(Long userId);
  //check user có đang học khóa học đó không
  boolean existsByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
