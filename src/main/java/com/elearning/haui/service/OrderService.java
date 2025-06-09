package com.elearning.haui.service;

import com.elearning.haui.domain.dto.*;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.entity.OrderDetail;
import com.elearning.haui.enums.OrderStatus;
import com.elearning.haui.repository.OrderDetailRepository;
import com.elearning.haui.repository.OrderRepository;

import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    public Page<Order> findPaginated(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }

    public List<Order> getAllOrderById() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(Long id) { return this.orderRepository.findByOrderId(id); }

    public OrderDTO getOrderDTOById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return convertToOrderDTO(order);
    }

    public void handleSaveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public void updateOrder(Order order) {
        this.orderRepository.save(order);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        Order order = this.orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        this.orderRepository.delete(order);
    }

    public CourseRepone  convertToOrderDTO(Course course) {
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

    public ResultPaginationDTO fetchAllCourses(Pageable pageable,String status) {
        Page<Order> pageOrders=null;
        if(status==null){
            pageOrders = orderRepository.findAll(pageable);
        }
        else{
            pageOrders=orderRepository.getOdersByStatus(status, pageable);
        }
    
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageOrders.getNumber() + 1);
        meta.setTotal(pageOrders.getTotalElements());

        meta.setPages(pageOrders.getTotalPages());
        meta.setTotal(pageOrders.getTotalElements());

        rs.setMeta(meta);
        List<OrderDTO> orderDTOs = pageOrders.getContent().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());

        rs.setResult(orderDTOs);

        return rs;
    }

    public OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserName(order.getUser().getName());
        dto.setOrderDate(order.getCreatedAt());
        dto.setOrderStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        List<Course> Courses=new ArrayList<>();
        for(OrderDetail od : order.getOrderDetails())
        {
            Courses.add(od.getCourse());
        }
        List<CourseRepone> CoursesRespone=mapToCoursesDTO(Courses);
        dto.setCoursesInOrder(CoursesRespone);
        return dto;
    }
    //map courseRespone
     public List<CourseRepone> mapToCoursesDTO(List<Course> listCourses){
        List<CourseRepone> dtos=new ArrayList<>();
        for(Course c : listCourses){
            CourseRepone dto=mapToCourseDTO(c);
            dtos.add(dto);
        }
        return dtos;
    }
     //map courseRespone
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
                    course.getCreatedAt());
    }


    @Transactional
    public void updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        try {
            String newStatus =status.toUpperCase();
            order.setStatus(newStatus);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
        }

    }

    //get Purcharse History
    
}
