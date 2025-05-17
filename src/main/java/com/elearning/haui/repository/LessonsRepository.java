package com.elearning.haui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Lessons;
@Repository
public interface LessonsRepository extends JpaRepository<Lessons,Long>{
    // @Query("""
    //         select l from Lessons l
    //         where l.
    //         """)
    // List<Lessons> getAllLessonsByTeacher()
}
