package com.elearning.haui.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
        
        
        @Query("""
                        select COUNT(c) from Course c
                        where  c.approvalStatus=:Status
                        """)
        int countCourseByStatus (@Param ("Status") String status);


        @Query("""
                        select c from Course c
                        where c.courseId=:courseId
                        """)
        Course findCourseByAdmin(@Param("courseId") Long courseId);

        Course save(Course course);
        @Query("""
                        SELECT c from Course c
                        where  c.approvalStatus='approved'   
                        """)
        List<Course> findAll();
        //get all course by admin
        @Query("""
                        SELECT c from Course c 
                        """)
        Page<Course> findAllByAdmin(Pageable pageable);
        
        //get all course by user
        @Query("""
                SELECT c FROM Course c
                WHERE c.approvalStatus = :Status
                """)
        Page<Course> findAllByStatus(@Param("Status") String Status, Pageable pageable);

        void deleteById(Long id);
        //get courseid by User
        @Query("""
                SELECT c FROM Course c 
                WHERE c.approvalStatus = 'approved' 
                AND c.courseId = :courseId
        """)
        Course findCourseByCourseId(@Param("courseId") Long courseId);
       
        //get course by category id
        @Query(value = """
                SELECT c.* 
                FROM courses c 
                JOIN coursecategories cc ON c.course_id = cc.course_id 
                WHERE cc.category_id = :categoryId 
                AND c.approval_status = 'approved'
                ORDER BY RAND()
                """, nativeQuery = true)
        List<Course> findCoursesByCategory(@Param("categoryId") Long categoryId);


        // Lọc khoá học
        // Lọc khóa học theo hour
        @Query("""
                SELECT c FROM Course c
                WHERE FLOOR(c.hour) = :hour
                AND c.approvalStatus = 'approved'
                """)
        List<Course> findByHour(@Param("hour") int hour);


        // Lọc khóa học theo giá (từ minPrice đến maxPrice)
        @Query("""
                SELECT c FROM Course c
                WHERE c.price BETWEEN :minPrice AND :maxPrice
                AND c.approvalStatus = 'approved'
                """)
                List<Course> findApprovedCoursesByPriceBetween(@Param("minPrice") double minPrice,
                                                        @Param("maxPrice") double maxPrice);

        // Lọc khóa học theo star (3 lựa chọn: >= 3, >= 4, 5)
        @Query("""
                SELECT c FROM Course c
                WHERE c.star >= :star
                AND c.approvalStatus = 'approved'
                """)
        List<Course> findApprovedCoursesByStar(@Param("star") float star);

        // Lọc khóa học theo thuộc tính paid/free (Giả sử "paid" là khóa học có giá,
        // "free" là khóa học miễn phí)
        @Query("SELECT c FROM Course c WHERE c.price = 0 and c.approvalStatus = 'approved' ")
        List<Course> findFreeCourses();

        @Query("SELECT c FROM Course c WHERE c.price > 0 and c.approvalStatus = 'approved'")
        List<Course> findPaidCourses();

        //Get all course created by Teacher
        @Query("""
                select c from Course c
                where c.author.username=:authorName
                """)
        List<Course> getAllCourseByTeacher(@Param ("authorName") String authorName);

        //get all course by Status for teacher
        @Query("""
                select c from Course c
                where c.author.username=:authorName
                and   c.approvalStatus =:Status
                """)
        List<Course> getAllCourseByStatusForTeacher(@Param ("authorName") String authorName,String Status);
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
