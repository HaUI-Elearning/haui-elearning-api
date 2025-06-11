package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.CourseRepone;
import com.elearning.haui.domain.dto.HistoryPurcharseDTO;
import com.elearning.haui.domain.dto.PaymentDTO;
import com.elearning.haui.domain.entity.Cart;
import com.elearning.haui.domain.entity.CartDetail;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.entity.OrderDetail;
import com.elearning.haui.domain.entity.Payment;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CartDetailRepository;
import com.elearning.haui.repository.CartRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
import com.elearning.haui.repository.OrderDetailRepository;
import com.elearning.haui.repository.OrderRepository;
import com.elearning.haui.repository.PaymentRepository;
import com.elearning.haui.repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class paymentsService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    //check course avalable with price than 0
    public boolean checkCourse(List<Course> listCourse, User user) {
        if (listCourse == null || listCourse.isEmpty() || user == null || user.getRole() == null) {
            return false;
        }
        for (Course c : listCourse) {
            if (c == null || c.getAuthor() == null) {
                return false;
            }
            if(c.getApprovalStatus().equals("pending")||c.getApprovalStatus().equals("rejected"))
            {
               return false;
            }
            if ("TEACHER".equals(user.getRole().getName()) && user.getName() != null 
                && user.getName().equals(c.getAuthor().getName()) || c.getPrice() <= 0) {
                return false;
            }
        }
        return true;
    }
    @Transactional
    public Order createOrder(String username, List<Long> courseIds, boolean viaCart) {
        if (username == null || courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("Username or course IDs cannot be null or empty");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Course> listCourse = courseRepository.findAllById(courseIds);
        if (listCourse == null || listCourse.isEmpty()) {
            throw new IllegalArgumentException("No courses found for the given IDs");
        }

        if (!checkCourse(listCourse, user)) {
            throw new IllegalStateException("Cannot make transactions with free or self-authored courses or course not approved");
        }

        if (viaCart) {
            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalStateException("Cart not found"));
            List<Long> cartCourseIds = cart.getCartDetails() != null
                    ? cart.getCartDetails().stream()
                    .map(cd -> cd.getCourse().getCourseId())
                    .collect(Collectors.toList())
                    : new ArrayList<>();
            if (cartCourseIds.size() != courseIds.size() || !cartCourseIds.containsAll(courseIds)) {
                throw new IllegalStateException("Cart details do not match selected courses");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("pending");
        order.setViaCart(viaCart);

        double totalAmount = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Long courseId : courseIds) {
            Course course = courseRepository.findCourseByCourseId(courseId);
            if (course == null) {
                throw new IllegalArgumentException("Course not found: " + courseId);
            }
            if (enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(user.getUserId(), courseId)) {
                continue;
            }
            OrderDetail detail = new OrderDetail();
            detail.setCourse(course);
            detail.setPrice(course.getPrice());
            totalAmount += course.getPrice();
            orderDetails.add(detail);
        }

        if (orderDetails.isEmpty()) {
            throw new IllegalStateException("You have already purchased all selected courses");
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order);
            orderDetailRepository.save(detail);
        }

        return order;
    }
    @Transactional
    public Payment createPayment(Order order, String txnRef) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("processing");  
        payment.setTotalAmount(order.getTotalAmount());
        payment.setTxnRef(txnRef);
        payment.setResponseCode(null); 

        return paymentRepository.save(payment);
    }

    @Transactional
    public void updatePaymentStatus(Long paymentId, String status, String responseCode) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        payment.setResponseCode(responseCode);

        paymentRepository.save(payment);
    }

    //History purcharse course
    
    public PaymentDTO mapPaymentToDTO(Payment p){
        PaymentDTO dto=new PaymentDTO();
        dto.setPaymentDate(p.getPaymentDate());
        dto.setStatus(p.getStatus());
        dto.setTotalAmount(p.getTotalAmount());
        dto.setTxnRef(p.getTxnRef());
        dto.setPaymentId(p.getPaymentId());
        return dto;
    
    }

    public List<CourseRepone> mapToCoursesDTO(List<Course> listCourses){
        List<CourseRepone> dtos=new ArrayList<>();
        for(Course c : listCourses){
            CourseRepone dto=mapToCourseDTO(c);
            dtos.add(dto);
        }
        return dtos;
    }
    public CourseRepone mapToCourseDTO(Course course) {
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
                    course.getCreatedAt(),
                    course.getApprovalStatus());
    }
    public List<HistoryPurcharseDTO> mapperListDTO(List<Payment> payments) {
    List<HistoryPurcharseDTO> dtos = new ArrayList<>();
    
   
    for (Payment p : payments) { 
        List<Course> courses =new ArrayList<>(); 
        for(OrderDetail od : p.getOrder().getOrderDetails()){
            courses.add(od.getCourse());
        }
        HistoryPurcharseDTO dto = mapperDTO(p.getOrder(), p, courses);
        dtos.add(dto);
    }

    return dtos;
}


    public HistoryPurcharseDTO mapperDTO(Order o,Payment Payments,List<Course> Courses)
    {
        List<CourseRepone> courseDTO=mapToCoursesDTO(Courses);
        PaymentDTO paymentDTO=mapPaymentToDTO(Payments);
        HistoryPurcharseDTO dto=new HistoryPurcharseDTO();
        dto.setCourses(courseDTO);
        dto.setPayment(paymentDTO);
        dto.setOrderId(o.getOrderId());
        dto.setOrderStatus(o.getStatus());
        dto.setTotalAmount(o.getTotalAmount());
        return dto;
    }
    //get all by User
    public List<HistoryPurcharseDTO> getAllHistoryPurcharses(String username) {
        List<Payment> listPayment = paymentRepository.findByUser(username);
        return mapperListDTO(listPayment);
    }

    //get all by status
    public List<HistoryPurcharseDTO> getAllHistoryPurcharsesByStatus(String username,String status) {
        List<Payment> listPayment = paymentRepository.findByUserAndStatus(username, status);
        return mapperListDTO(listPayment);
    }
    
    // get by id
    public HistoryPurcharseDTO getById(String username,Long Paymentid){
        Payment payment=paymentRepository.findByUsernameAndPaymentid(username, Paymentid);
        if(payment==null){
            throw new RuntimeException("Payment not found");
        }
        Order order=payment.getOrder();
        List<Course> courses =new ArrayList<>(); 
        for(OrderDetail od : order.getOrderDetails()){
            courses.add(od.getCourse());
        }
        return mapperDTO(order, payment, courses);
    }

    
}
