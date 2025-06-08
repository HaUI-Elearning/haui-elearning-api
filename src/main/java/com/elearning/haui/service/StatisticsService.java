package com.elearning.haui.service;

import com.elearning.haui.domain.dto.UserGrowthDTO;
import com.elearning.haui.enums.OrderStatus;
import com.elearning.haui.repository.OrderRepository;
import com.elearning.haui.repository.PaymentRepository;
import com.elearning.haui.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired 
    PaymentRepository paymentRepository;
    @Autowired
    UserRepository userRepository;
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
        stats.put("TotalTeachers",userRepository.toTalTeacher());
        // Lấy tổng doanh thu
        Double totalRevenue = this.dashboardService.getTotalRevenue();
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Định dạng với 2 chữ số thập phân
        String formattedRevenue = df.format(totalRevenue);
        stats.put("totalRevenue", formattedRevenue != null ? formattedRevenue : 0.0);

        // Lấy số lượng đơn thanh toán
       // long completedOrders = orderRepository.countByStatus(OrderStatus.COMPLETED);
        //stats.put("totalCompletedOrders", completedOrders);
        
        YearMonth currentYearMonth = YearMonth.now();
        LocalDateTime startDate = currentYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentYearMonth.atEndOfMonth().atTime(23, 59, 59, 999_999_999);
        Double revenueMonth = paymentRepository.getMonthlyRevenue(startDate, endDate);
        // Lấy số lượng đơn hàng
        
        stats.put("revenueMonth", revenueMonth);
        long totalOrders = orderRepository.count();
        stats.put("totalOrders", totalOrders);

        long newUsersCount = userRepository.countNewUsersBetween(startDate, endDate);
        long newTeachersCount = userRepository.countNewTeachersBetween(startDate, endDate);
        stats.put("newUsersCount", newUsersCount);
        stats.put("newTeachersCount",newTeachersCount);

        return stats;
    }

    public UserGrowthDTO getNewUserAndTeacherGrowthForCurrentMonth() {
       
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();

       
        LocalDateTime startDate = currentYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentYearMonth.atEndOfMonth().atTime(23, 59, 59, 999_999_999);

        long newUsersCount = userRepository.countNewUsersBetween(startDate, endDate);
        long newTeachersCount = userRepository.countNewTeachersBetween(startDate, endDate);

        return new UserGrowthDTO(newUsersCount, newTeachersCount, currentMonth, currentYear);
    }
}
