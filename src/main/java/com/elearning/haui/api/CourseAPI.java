package com.elearning.haui.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.CategoryCourseDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.service.CategoryService;
import com.elearning.haui.service.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseAPI {
    private final CategoryService categoryService;
    private final CourseService courseService;

    public CourseAPI(CategoryService categoryService, CourseService courseService) {
        this.categoryService = categoryService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseDetail(@PathVariable Long id)
            throws RuntimeException {

        // Gọi service để lấy thông tin khóa học
        CourseDTO courseDetail = courseService.getCourseDetail(id);

        // Trả về thông tin khóa học
        return ResponseEntity.ok(courseDetail);

    }

    @GetMapping("/categorycourse")
    public ResponseEntity<List<CategoryCourseDTO>> getCategoryCourse() {
        List<Category> categories = categoryService.getAllCategories();

        // Duyệt qua từng thể loại và ánh xạ sang CategoryCourseDTO
        List<CategoryCourseDTO> categoryCourseDTOs = categories.stream().map(category -> {

            List<CourseDTO> courses = courseService.getCoursesByCategoryWithLimit(category.getCategoryId());

            return new CategoryCourseDTO(category.getCategoryId(), category.getName(), courses);
        }).collect(Collectors.toList());

        // Trả về danh sách với HTTP 200 OK
        return ResponseEntity.ok(categoryCourseDTOs);
    }

    @GetMapping("/search")
    public ResultPaginationDTO searchCourses(
            @RequestParam(required = false) String hourRange,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) String starRating,
            @RequestParam(required = false) String categoryId,
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        // Gọi phương thức getCourses với phân trang và các tham số lọc
        return courseService.getCourses(hourRange, minPrice, maxPrice, isPaid, starRating, categoryId, pageable);
    }

    @GetMapping("/category/{categoryId}")
    public ResultPaginationDTO searchCoursesByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        // Gọi phương thức getCourses với phân trang và các tham số lọc
        return courseService.getCoursesByCategory(categoryId, pageable);
    }
}
