package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.haui.entity.Category;
import com.elearning.haui.entity.CourseCategory;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    List<CourseCategory> findByCategory(Category category);

    // Đếm số lượng khóa học theo thể loại
    long countByCategory(Category category);
}
