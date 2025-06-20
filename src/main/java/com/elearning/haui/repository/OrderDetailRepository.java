package com.elearning.haui.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.domain.entity.OrderDetail;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

 @Query("SELECT new com.elearning.haui.domain.dto.CourseSalesDTO(" +
       "c.courseId, c.name, cat.name, c.price, COUNT(DISTINCT o.user), SUM(od.price), author.name) " + 
       "FROM OrderDetail od " +
       "JOIN od.course c " +
       "JOIN c.author author " +
       "JOIN CourseCategory cc ON cc.course.courseId = c.courseId " +
       "JOIN cc.category cat " +
       "JOIN od.order o " + 
       "WHERE o.status IN ('paid', 'free')" +
       "GROUP BY c.courseId, c.name, cat.name, c.price, author.name " +
       "ORDER BY COUNT(DISTINCT o.user) DESC") 
Page<CourseSalesDTO> findTopSellingCourses(Pageable pageable);


}
