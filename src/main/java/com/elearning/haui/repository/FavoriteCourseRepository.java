package com.elearning.haui.repository;

import com.elearning.haui.domain.entity.FavoriteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse, Long> {
    List<FavoriteCourse> findByUser_UserId(Long userId);

   @Query("""
    select f from FavoriteCourse f 
    where f.course.courseId = :courseId and f.user.userId = :userId
""")
FavoriteCourse findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);
}
