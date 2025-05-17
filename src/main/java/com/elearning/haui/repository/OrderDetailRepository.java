package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.domain.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT new com.elearning.haui.domain.dto.CourseSalesDTO(" +
            "od.course.courseId, " +
            "od.course.name, " +
            "cc.category.name, " + // Lấy tên thể loại từ CourseCategory
            "od.course.price, " + // Lấy giá từ Course
            "SUM(od.quantity), " + // Tính tổng số lượng bán
            "SUM(od.quantity * od.course.price), " + // Tính doanh thu
            "od.course.author.name) " + // Lấy tên tác giả từ Course
            "FROM OrderDetail od " +
            "JOIN CourseCategory cc ON cc.course.courseId = od.course.courseId " + // Kết nối với CourseCategory
            "JOIN Category cat ON cat.categoryId = cc.category.categoryId " + // Liên kết với bảng Category
            "JOIN od.order o " +
            "WHERE o.status = 'completed' " + // Thêm điều kiện status
            "GROUP BY od.course.courseId, od.course.name, cc.category.name, od.course.price, od.course.author " +
            "ORDER BY SUM(od.quantity) DESC")
    List<CourseSalesDTO> findTopSellingCourses();

}
