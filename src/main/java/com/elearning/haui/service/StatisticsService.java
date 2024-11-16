package com.elearning.haui.service;

import com.elearning.haui.enums.OrderStatus;
import com.elearning.haui.repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final DashboardService dashboardService;

    public StatisticsService(OrderRepository orderRepository, DashboardService dashboardService) {
        this.orderRepository = orderRepository;
        this.dashboardService = dashboardService;
    }

    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Lấy số lượng người dùng
        long userCount = this.dashboardService.getTotalUserCount();
        stats.put("totalUsers", userCount);

        // Lấy số lượng khóa học
        long courseCount = this.dashboardService.getTotalCourseCount();
        stats.put("totalCourses", courseCount);

        // Lấy tổng doanh thu
        Double totalRevenue = this.dashboardService.getTotalRevenue();
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Định dạng với 2 chữ số thập phân
        String formattedRevenue = df.format(totalRevenue);
        stats.put("totalRevenue", formattedRevenue != null ? formattedRevenue : 0.0);

        // Lấy số lượng đơn thanh toán
        long completedOrders = orderRepository.countByStatus(OrderStatus.COMPLETED);
        stats.put("totalCompletedOrders", completedOrders);

        // Lấy số lượng đơn hàng
        long totalOrders = orderRepository.count();
        stats.put("totalOrders", totalOrders);

        return stats;
    }
}
