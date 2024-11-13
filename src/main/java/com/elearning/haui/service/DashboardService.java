package com.elearning.haui.service;

import org.springframework.stereotype.Service;

import com.elearning.haui.repository.OrderRepository;

@Service
public class DashboardService {
    private final UserService userService;
    private final CourseService courseService;
    private final OrderRepository orderRepository;

    public DashboardService(UserService userService, CourseService courseService, OrderRepository orderRepository) {
        this.userService = userService;
        this.courseService = courseService;
        this.orderRepository = orderRepository;
    }

    public long getTotalUserCount() {
        return this.userService.countUsers();
    }

    public long getTotalCourseCount() {
        return this.courseService.countCours();
    }

    public double getTotalRevenue() {
        return this.orderRepository.getTotalRevenue();
    }

}
