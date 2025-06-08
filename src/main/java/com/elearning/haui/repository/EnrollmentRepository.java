package com.elearning.haui.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Enrollment;
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
  
  @Query("""
      SELECT e FROM Enrollment e
      JOIN e.course c
      WHERE c.author.username = :username
  """)
  List<Enrollment> getEnrollmentsByTeacher(@Param("username") String username);

  //course statistics by Teacher
  @Query("SELECT e.course.courseId, COUNT(e) " +
           "FROM Enrollment e " +
           "WHERE e.course.author.username = :username " +
           "AND YEAR(e.enrollmentDate) = :year " +
           "AND MONTH(e.enrollmentDate) = :month " +
           "GROUP BY e.course.courseId")
    List<Object[]> countEnrollmentsByCourseAndMonth(String username, int month, int year);

    // Đếm tổng số lượt ghi danh của một khóa học cho đến cuối một tháng/năm cụ thể
@Query("SELECT e.course.courseId, COUNT(e) " +
       "FROM Enrollment e " +
       "WHERE e.course.author.username = :username " +
       "AND e.enrollmentDate <= :endDate " +
       "GROUP BY e.course.courseId")
List<Object[]> countTotalEnrollmentsUpToDate(@Param("username") String username, @Param("endDate") LocalDateTime endDate);


}
