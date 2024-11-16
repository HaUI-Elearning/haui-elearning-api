package com.elearning.haui.service;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.repository.CategoryRepository;
import com.elearning.haui.repository.CourseCategoryRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.OrderDetailRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
