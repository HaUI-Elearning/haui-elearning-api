package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Lessons;
@Repository
public interface LessonsRepository extends JpaRepository<Lessons,Long>{
    @Query("""
        SELECT l FROM Lessons l
        JOIN l.chapter c
        JOIN c.course co
        WHERE c.chapterId = :chapterId
        AND co.author.username = :username
         """)
    List<Lessons> getAllLessonsByChapterAndAuthor(@Param("chapterId") Long chapterId,
                                            @Param("username") String username);


    @Query("""
        SELECT l FROM Lessons l
        JOIN l.chapter c
        JOIN c.course co
        WHERE c.chapterId = :chapterId
        AND co.author.username = :username
        AND l.lessonId=:lessonId
         """)
    Lessons getLessonsById(@Param("username") String username,
                            @Param("chapterId") Long chapterId,
                            @Param("lessonId") Long lessonId);  
    
    @Query("""
    select count(l) from Lessons l
        JOIN l.chapter c
        JOIN c.course co
        WHERE c.chapterId = :chapterId
        AND co.author.username = :username
    """)
    int countLessonsByCourseAndAuthor(@Param("username") String username,
                            @Param("chapterId") Long chapterId);
}
