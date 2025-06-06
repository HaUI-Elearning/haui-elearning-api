package com.elearning.haui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            "ORDER BY RAND() ", nativeQuery = true)
    List<Course> findCoursesByCategory(@Param("categoryId") Long categoryId);

    // Lọc khoá học
    // Lọc khóa học theo hour
    List<Course> findByHour(int hour);

    // Lọc khóa học theo giá (từ minPrice đến maxPrice)
    List<Course> findByPriceBetween(double minPrice, double maxPrice);

    // Lọc khóa học theo star (3 lựa chọn: >= 3, >= 4, 5)
    List<Course> findByStarGreaterThanEqual(float star);

    // Lọc khóa học theo thuộc tính paid/free (Giả sử "paid" là khóa học có giá,
    // "free" là khóa học miễn phí)
    @Query("SELECT c FROM Course c WHERE c.price = 0")
    List<Course> findFreeCourses();

    @Query("SELECT c FROM Course c WHERE c.price > 0")
    List<Course> findPaidCourses();

    //Get all course created by Teacher
    @Query("""
            select c from Course c
            where c.author.username=:authorName
             """)
    List<Course> getAllCourseByTeacher(@Param ("authorName") String authorName);
    //Get course by Teacher
    @Query("""
            SELECT c FROM Course c 
            WHERE c.author.username=:authorName
            and c.courseId=:courseId
            """)
    Course getCoursesIdByTeacher(@Param ("authorName") String authorName
                                    , @Param("courseId") Long courseId);

        @Query("""
        select count(c) > 0 from Course c
        where c.courseId = :courseId and c.author.username = :authorName
        """)
        boolean existsByCourseIdAndAuthor(@Param("courseId") Long courseId,
                                        @Param("authorName") String authorName);
        @Query("UPDATE Course c SET c.sold = c.sold + 1 WHERE c.courseId = :courseId")
        @Modifying
        void updateSold(@Param("courseId") Long courseId);
}
