package com.elearning.haui.api;

import java.util.List;
import java.util.Map;

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
   @GetMapping("/sales/top-selling-courses")
public ResponseEntity<List<CourseSalesDTO>> getTopSellingCourses(
        @RequestParam(required = false, defaultValue = "10") int limit) {
    List<CourseSalesDTO> topSellingCourses = salesReportService.getTopSellingCourses(limit);

    if (topSellingCourses.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.ok(topSellingCourses);
}
}