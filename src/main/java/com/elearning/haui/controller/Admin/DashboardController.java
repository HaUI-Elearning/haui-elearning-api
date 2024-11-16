package com.elearning.haui.controller.Admin;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.service.CourseService;
import com.elearning.haui.service.DashboardService;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final CourseService courseService;

    public DashboardController(DashboardService dashboardService, CourseService courseService) {
        this.dashboardService = dashboardService;
        this.courseService = courseService;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        // Lấy các thống kê từ DashboardService
        long userCount = dashboardService.getTotalUserCount();
        long courseCount = dashboardService.getTotalCourseCount();
        double totalRevenue = dashboardService.getTotalRevenue();

        // Lấy danh sách sản phẩm bán chạy nhất từ CourseService
        List<CourseSalesDTO> topSellingCourses = courseService.getTopSellingCourses();

        // Lấy thống kê số lượng khóa học theo thể loại (giả sử trả về
        // Map<String,Integer>)
        Map<String, Integer> categoryStats = courseService.getCourseCountByCategory();

        // Chuyển đổi Map thành JSON string
        // Khai báo ObjectMapper và xử lý ngoại lệ JsonProcessingException
        ObjectMapper objectMapper = new ObjectMapper();
        String categoryStatsJson = "";
        try {
            categoryStatsJson = objectMapper.writeValueAsString(categoryStats); // Chuyển đổi Map thành JSON string
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }

        // Thêm các giá trị vào model
        model.addAttribute("userCount", userCount);
        model.addAttribute("courseCount", courseCount);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("categoryStatsJson", categoryStatsJson);
        model.addAttribute("topSellingCourses", topSellingCourses);

        // Trả về tên view (tệp JSP)
        return "/admin/dashboard/charts";
    }
}
