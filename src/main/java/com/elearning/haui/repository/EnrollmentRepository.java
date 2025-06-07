package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.User;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  List<Enrollment> findByUser_UserId(Long userId);
  //check user có đang học khóa học đó không
  boolean existsByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
  //get list  course participants
 @Query("""
            select er from Enrollment er
            join er.course c
            where c.courseId = :courseId
            and c.author.username = :username
           """)
  List<Enrollment> getParticipantsByCourseId(@Param("username") String username, @Param("courseId") Long courseId);

}
