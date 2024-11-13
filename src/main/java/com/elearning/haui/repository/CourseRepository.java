package com.elearning.haui.repository;

import com.elearning.haui.entity.Category;
import com.elearning.haui.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course save(Course course);

    List<Course> findAll();

    void deleteById(Long id);

    Course findCourseByCourseId(Long courseId);

}
