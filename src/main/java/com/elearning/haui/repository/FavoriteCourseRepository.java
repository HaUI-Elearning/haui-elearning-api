package com.elearning.haui.repository;

import com.elearning.haui.domain.entity.FavoriteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse, Long> {
    List<FavoriteCourse> findByUser_UserId(Long userId);
}
