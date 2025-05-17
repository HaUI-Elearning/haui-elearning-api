package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Chapters;
@Repository
public interface ChaptersRepository extends JpaRepository<Chapters,Long> {
    @Query("""
    select c from Chapters c
    where c.course.courseId = :courseId
    and c.course.author.username = :authorName
    order by c.position asc
    """)
List<Chapters> getAllChaptersByTeacher(@Param("courseId") Long courseId,
                                       @Param("authorName") String authorName);

@Query("""
    select c from Chapters c
    where c.course.courseId = :courseId
    and c.chapterId = :chapterId
    and c.course.author.username = :authorName
    """)
Chapters getChapterById(@Param("courseId") Long courseId,
                        @Param("chapterId") Long chapterId,
                        @Param("authorName") String authorName);
@Query("""
    select count(c) from Chapters c
    where c.course.courseId = :courseId
    and c.course.author.username = :authorName
    """)
int countChaptersByCourseAndAuthor(@Param("courseId") Long courseId,
                                   @Param("authorName") String authorName);
}
