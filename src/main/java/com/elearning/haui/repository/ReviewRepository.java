package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    //get all review for course
    @Query("SELECT r FROM Review r WHERE r.course.courseId = :courseId")
    List<Review> findReviewsByCourseId(@Param("courseId") Long courseId);
    //caculatate avg (Rating)
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = :courseId")
    Double getAverageRatingByCourseId(@Param("courseId") Long courseId);
    //fillter review by stars
   @Query("SELECT r FROM Review r WHERE r.rating >= :min AND r.rating < :max AND r.course.courseId = :courseId")
    List<Review> findReviewsByRatingRangeForCourseId(
        @Param("min") double min,
        @Param("max") double max,
        @Param("courseId") Long courseId
    );
}
