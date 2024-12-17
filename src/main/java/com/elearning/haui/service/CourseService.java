package com.elearning.haui.service;

import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.domain.dto.Meta;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.repository.CategoryRepository;
import com.elearning.haui.repository.CourseCategoryRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.OrderDetailRepository;
import com.elearning.haui.utils.PaginationUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final OrderDetailRepository orderDetailRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository,
            CourseCategoryRepository courseCategoryRepository, OrderDetailRepository orderDetailRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void handleSaveProduct(Course course) {
        this.courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    public void deleteCourseById(Long id) {
        this.courseRepository.deleteById(id);
    }

    public Course getCourseById(Long id) {
        return this.courseRepository.findCourseByCourseId(id);
    }

    public Page<Course> findPaginated(PageRequest pageRequest) {
        return this.courseRepository.findAll(pageRequest);
    }

    public CourseDTO convertToCourseDTO(Course course) {
        return new CourseDTO(
                course.getCourseId(),
                course.getName(),
                course.getThumbnail(),
                course.getDescription(),
                course.getContents(),
                course.getStar(),
                course.getHour(),
                course.getPrice(),
                course.getAuthor(),
                course.getChapters(),
                course.getCreatedAt());
    }

    // đếm số lượng khoá học
    public long countCours() {
        return this.courseRepository.count();
    }

    // Lấy số lượng khóa học theo thể loại
    public Map<String, Integer> getCourseCountByCategory() {
        // Lấy tất cả các thể loại từ cơ sở dữ liệu
        List<Category> categories = categoryRepository.findAll();

        // Khởi tạo Map để lưu trữ số lượng khóa học theo thể loại
        Map<String, Integer> categoryCountMap = new HashMap<>();

        // Duyệt qua các thể loại và đếm số lượng khóa học trong từng thể loại
        for (Category category : categories) {
            long count = courseCategoryRepository.countByCategory(category); // Đếm số lượng khóa học thuộc thể loại này
            categoryCountMap.put(category.getName(), (int) count); // Thêm vào Map
        }

        // Trả về Map chứa thông tin số lượng khóa học theo thể loại
        return categoryCountMap;
    }

    // Lấy danh sách khóa học bán nhiều nhất
    public List<CourseSalesDTO> getTopSellingCourses() {
        return orderDetailRepository.findTopSellingCourses();
    }

    // Phương thức lấy danh sách khóa học theo categoryId và giới hạn số lượng kết
    // quả
    public ResultPaginationDTO getCoursesByCategory(Long categoryId, Pageable pageable) {
        // Lấy tất cả khóa học theo categoryId từ repository
        List<Course> allCourses = courseRepository.findCoursesByCategory(categoryId);

        return PaginationUtils.paginate(allCourses, pageable, this::convertToCourseDTO);
    }

    public List<CourseDTO> getCoursesByCategoryWithLimit(Long categoryId) {
        List<Course> limitedCourses = courseRepository.findCoursesByCategory(categoryId);

        // Shuffle và chọn ngẫu nhiên `limit` khóa học
        Collections.shuffle(limitedCourses);
        List<Course> randomCourses = limitedCourses.stream()
                .limit(10)
                .collect(Collectors.toList());

        // Ánh xạ sang CourseDTO
        return randomCourses.stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    // Xem chi tiết một khoá học ở phía clients
    public CourseDTO getCourseDetail(Long courseId) {
        // Tìm khóa học theo ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        // Ánh xạ sang DTO
        return convertToCourseDTO(course);
    }

    // search course
    public ResultPaginationDTO getCourses(String hourRange, Double minPrice, Double maxPrice, Boolean isPaid,
            String starRating, String categoryId, Pageable pageable) {
        // Lấy toàn bộ danh sách các khóa học từ cơ sở dữ liệu
        List<Course> allCourses = null;
        if (categoryId == null) {
            allCourses = courseRepository.findAll();
        } else {
            allCourses = courseRepository.findCoursesByCategory(Long.parseLong(categoryId));
        }

        // Lọc theo giờ (chỉ lọc nếu hourRange không phải null)
        if (hourRange != null && !hourRange.isEmpty()) {
            allCourses = filterByHourRange(allCourses, hourRange);
        }

        // Lọc theo giá nếu có
        if (minPrice != null && maxPrice != null) {
            allCourses = filterByPrice(allCourses, minPrice, maxPrice);
        }

        // Lọc theo starRating
        if (starRating != null) {
            allCourses = filterByStarRating(allCourses, starRating);
        }

        // Lọc theo paid/free
        if (isPaid != null) {
            allCourses = filterByPaidStatus(allCourses, isPaid);
        }

        // Sử dụng PaginationUtils để phân trang
        return PaginationUtils.paginate(allCourses, pageable, this::convertToCourseDTO);
    }

    // Lọc theo khoảng giờ
    private List<Course> filterByHourRange(List<Course> courses, String hourRange) {
        switch (hourRange) {
            case "3-6":
                return courses.stream()
                        .filter(course -> course.getHour() >= 3 && course.getHour() < 6)
                        .collect(Collectors.toList());
            case "6-9":
                return courses.stream()
                        .filter(course -> course.getHour() >= 6 && course.getHour() < 9)
                        .collect(Collectors.toList());
            case "9-12":
                return courses.stream()
                        .filter(course -> course.getHour() >= 9 && course.getHour() < 12)
                        .collect(Collectors.toList());
            case "more":
                return courses.stream()
                        .filter(course -> course.getHour() >= 12)
                        .collect(Collectors.toList());
            default:
                return courses;
        }
    }

    // Lọc theo giá
    private List<Course> filterByPrice(List<Course> courses, double minPrice, double maxPrice) {
        return courses.stream()
                .filter(course -> course.getPrice() >= minPrice && course.getPrice() <= maxPrice)
                .toList();
    }

    // Lọc theo star rating
    private List<Course> filterByStarRating(List<Course> courses, String starRating) {
        switch (starRating) {
            case ">=3":
                return courses.stream()
                        .filter(course -> course.getStar() >= 3)
                        .toList();
            case ">=4":
                return courses.stream()
                        .filter(course -> course.getStar() >= 4)
                        .toList();
            case "5":
                return courses.stream()
                        .filter(course -> course.getStar() == 5)
                        .toList();
            default:
                return courses;
        }
    }

    // Lọc theo paid/free
    private List<Course> filterByPaidStatus(List<Course> courses, boolean isPaid) {
        if (isPaid) {
            return courses.stream()
                    .filter(course -> course.getPrice() > 0)
                    .toList();
        } else {
            return courses.stream()
                    .filter(course -> course.getPrice() == 0)
                    .toList();
        }
    }

}
