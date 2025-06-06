package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

public String EnrollFreeCourse(String Username,Long CourseId){
    User user=userRepository.findByName(Username);
    if(user==null){
      throw new RuntimeException("User not found");
    }
    Course course=courseRepository.findCourseByCourseId(CourseId);
    if(course==null){
      throw new RuntimeException("Course not found");
    }
    if(course.getPrice()>0){
      throw new RuntimeException("This course is not free");
    }
    boolean isEnrolled=enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(user.getUserId(), CourseId);
      if(isEnrolled){
        throw new RuntimeException("already own the course cannot ,redo this action");
    }
    //create order
    Order order=new Order();
    order.setUser(user);
    order.setCreatedAt(LocalDateTime.now());
    order.setStatus("free");
    order.setTotalAmount(0.0);
    order.setViaCart(false);
    orderRepository.save(order);
   //orderdetail
    OrderDetail orderDetail=new OrderDetail();
    orderDetail.setOrder(order);
    orderDetail.setCourse(course);
    orderDetail.setPrice(course.getPrice());
    orderDetailRepository.save(orderDetail);
    //open free course for user
    Enrollment enrollment=new Enrollment();
    enrollment.setCourse(course);
    enrollment.setUser(user);
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollmentRepository.save(enrollment);
    return "Enroll free course success";
  }
}
