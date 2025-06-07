package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.HistoryPurcharseDTO;
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
    public boolean checkCourse(List<Course> listCourse){
        for(Course c :listCourse){
            if(c.getPrice()==0){
                return false;
            }
        }
        return true;
    }
    @Transactional
    public Order createOrder(String username, List<Long> courseIds,boolean viaCart) {
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Course> listCourse=courseRepository.findAllById(courseIds);
        if(!checkCourse(listCourse)){
            throw new RuntimeException("Cannot make transactions with 0 VND course");
        }
        if (viaCart) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<Long> cartCourseIds = cart.getCartDetails().stream()
                .map(cd -> cd.getCourse().getCourseId())
                .collect(Collectors.toList());
        
        if (cartCourseIds.size() != courseIds.size() || !cartCourseIds.containsAll(courseIds)) {
            throw new RuntimeException("Cart details do not match selected courses");
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
                throw new RuntimeException("Course not found");
            }
            boolean checkEnroll = enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(user.getUserId(), courseId);
            if(checkEnroll){
                continue;
            }
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order); 
            detail.setCourse(course);
            detail.setPrice(course.getPrice());  
            totalAmount += course.getPrice();
            orderDetails.add(detail);
        }
        if(orderDetails.isEmpty()){
            throw new RuntimeException("You have purchased that courses");
        }
        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        for (OrderDetail detail : orderDetails) {
            detail.setOrder(order);
            orderDetailRepository.save(detail);
        }

        return order;
    }

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
    public List<HistoryPurcharseDTO> mapperListDTO()
    {
        return null;
    }

    public HistoryPurcharseDTO mapperDTO()
    {
        return null;
    }


    public List<HistoryPurcharseDTO> getAllHistoryPurcharses(String Username){
        List<Payment> ListPayment=paymentRepository.findByUserIdAndStatus(Username,"success");
        if(ListPayment.isEmpty()){
            throw new RuntimeException("You not have payments");
        }
        for(Payment p : ListPayment){
           Order o = p.getOrder();
           
        }
        

        return null;
    }
    
}
