package com.elearning.haui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course save(Course course);

    List<Course> findAll();

    void deleteById(Long id);

    Course findCourseByCourseId(Long courseId);

    @Query(value = "SELECT c.* " +
            "FROM courses c " +
            "JOIN coursecategories cc ON c.course_id = cc.course_id " +
            "WHERE cc.category_id = :categoryId " +
            "ORDER BY RAND() " +
            "LIMIT 10", nativeQuery = true)
    List<Course> findLimitedCoursesByCategory(@Param("categoryId") Long categoryId);

}
