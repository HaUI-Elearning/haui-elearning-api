package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseRepone;
import com.elearning.haui.domain.dto.OrderDTO;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.entity.OrderDetail;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
import com.elearning.haui.repository.OrderDetailRepository;
import com.elearning.haui.repository.OrderRepository;
import com.elearning.haui.repository.UserRepository;;

@Service
public class EnrollmentService {

  @Autowired
  OrderRepository orderRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  OrderDetailRepository orderDetailRepository;
  private final EnrollmentRepository enrollmentRepository;

  public EnrollmentService(EnrollmentRepository enrollmentRepository) {
    this.enrollmentRepository = enrollmentRepository;
  }

  public List<Enrollment> getEnrollmentByUserId(Long userId) {
    return this.enrollmentRepository.findByUser_UserId(userId);
  }

  // Ánh xạ từ Course sang CourseRepone
  private CourseRepone mapToCourseDTO(Course course) {
   return new CourseRepone(
                course.getCourseId(),
                course.getName(),
                course.getThumbnail(),
                course.getDescription(),
                course.getContents(),
                course.getStar(),
                course.getHour(),
                course.getPrice(),
                course.getSold(),
                course.getAuthor().getName(),
                course.getCreatedAt());
  }

  // Lấy danh sách các khóa học mà người dùng đã tham gia và ánh xạ sang CourseDTO
  public List<CourseRepone> getCoursesByUserId(Long userId) {
    // Lấy tất cả các Enrollment của người dùng theo userId
    List<Enrollment> enrollments = this.enrollmentRepository.findByUser_UserId(userId);

    // Ánh xạ từ Enrollment sang CourseDTO
    return enrollments.stream()
        .map(enrollment -> mapToCourseDTO(enrollment.getCourse())) // ánh xạ từ course trong enrollment
        .collect(Collectors.toList());
  }

  // Enroll free course

  @Transactional
  public CourseRepone EnrollFreeCourse(String username, Long courseId) {
      if (username == null || courseId == null) {
          throw new IllegalArgumentException("Username or course ID cannot be null");
      }

      User user = userRepository.findByUsername(username);
      if (user == null) {
          throw new IllegalArgumentException("User not found");
      }

      Course course = courseRepository.findCourseByCourseId(courseId);
      if (course == null) {
          throw new IllegalArgumentException("Course not found");
      }
      if(course.getApprovalStatus().equals("pending")||course.getApprovalStatus().equals("rejected"))
        {
                throw new RuntimeException("This course is not approved.");
        }
      if (user.getRole() != null && "TEACHER".equals(user.getRole().getName()) 
              && user.getName() != null && course.getAuthor() != null 
              && user.getName().equals(course.getAuthor().getName())) {
          throw new IllegalStateException("You cannot enroll in your own course");
      }

      if (course.getPrice() > 0) {
          throw new IllegalStateException("This course is not free");
      }

      boolean isEnrolled = enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(user.getUserId(), courseId);
      if (isEnrolled) {
          throw new IllegalStateException("You already own this course and cannot enroll again");
      }

      // Create order (optional, consider if needed for free courses)
      Order order = new Order();
      order.setUser(user);
      order.setCreatedAt(LocalDateTime.now());
      order.setStatus("free");
      order.setTotalAmount(0.0);
      order.setViaCart(false);
      order = orderRepository.save(order);

      // Create order detail 
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setOrder(order);
      orderDetail.setCourse(course);
      orderDetail.setPrice(course.getPrice());
      orderDetailRepository.save(orderDetail);

      // Open free course for user
      Enrollment enrollment = new Enrollment();
      enrollment.setCourse(course);
      enrollment.setUser(user);
      enrollment.setEnrollmentDate(LocalDateTime.now());
      enrollmentRepository.save(enrollment);

      // Update sold
      courseRepository.updateSold(courseId);
      CourseRepone response = mapToCourseDTO(course);
      return response;
  }
}
