package com.elearning.haui.api;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.service.CourseService;
import com.elearning.haui.service.SalesReportService;
import com.elearning.haui.service.StatisticsService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportAPI {

    private final SalesReportService salesReportService;
    private final CourseService courseService;
    private final StatisticsService statisticsService;

    public ReportAPI(SalesReportService salesReportService, CourseService courseService,
            StatisticsService statisticsService) {
        this.salesReportService = salesReportService;
        this.courseService = courseService;
        this.statisticsService = statisticsService;
    }
    @Operation(summary = "Thống kê chung admin")
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> statistics = statisticsService.getDashboardStatistics();

        // Kiểm tra nếu không có dữ liệu
        if (statistics.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Trả về dữ liệu thống kê
        return ResponseEntity.status(HttpStatus.OK).body(statistics);
    }

    // Trả về số lượng khoá thể loại và số lượng khoá trong thể loại đó
    @Operation(summary = "Số lượng khóa của các danh mục")
    @GetMapping("/sales/course-count")
    public ResponseEntity<Map<String, Integer>> getCourseCountByCategory() {
        // Gọi service để lấy dữ liệu
        Map<String, Integer> courseCountByCategory = courseService.getCourseCountByCategory();

        // Kiểm tra nếu không có dữ liệu
        if (courseCountByCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Trả về 204 nếu không có dữ liệu
        }

        // Trả về danh sách thể loại và số lượng khóa học
        return ResponseEntity.status(HttpStatus.OK).body(courseCountByCategory);
    }

    // Trả về danh sách khóa học bán chạy
    @Operation(summary = "Lấy danh sách khóa học bán chạy nhất (có phân trang)")
    @GetMapping("/sales/top-selling-courses")
    public ResponseEntity<Page<CourseSalesDTO>> getTopSellingCourses(
            // 1. Thay đổi tham số: nhận vào page và size
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 2. Gọi phương thức service đã được cập nhật
        Page<CourseSalesDTO> coursePage = salesReportService.getTopSellingCourses(page, size);

        // 3. Trả về đối tượng Page trực tiếp
        return ResponseEntity.ok(coursePage);
    }
}