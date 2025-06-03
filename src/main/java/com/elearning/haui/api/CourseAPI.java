package com.elearning.haui.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.CategoryCourseDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.ReviewDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.repository.UserRepository;
import com.elearning.haui.service.CategoryService;
import com.elearning.haui.service.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseAPI {
    private final CategoryService categoryService;
    private final CourseService courseService;
    @Autowired
    UserRepository userRepository;
    public CourseAPI(CategoryService categoryService, CourseService courseService) {
        this.categoryService = categoryService;
        this.courseService = courseService;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof Jwt jwt) {
                return jwt.getClaimAsString("sub");
            } else {
                return authentication.getName();
            }
        }
        return null;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseDetail(@PathVariable Long id) {
    String username = getCurrentUsername();
    User user = userRepository.findByUsername(username);

    System.out.println("Username: " + username);

    Long userId = (user != null) ? user.getUserId() : null;

    CourseDTO courseDetail = courseService.getCourseDetail(id, userId);
    return ResponseEntity.ok(courseDetail);
}


    @GetMapping("/categorycourse")
    public ResponseEntity<List<CategoryCourseDTO>> getCategoryCourse() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        System.out.println("Username: " + username);
        Long userId = (user != null) ? user.getUserId() : null;
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryCourseDTO> categoryCourseDTOs = categories.stream().map(category -> {
            List<CourseDTO> courses = courseService.getCoursesByCategoryWithLimit(category.getCategoryId(), userId);
            return new CategoryCourseDTO(category.getCategoryId(), category.getName(), courses);
        }).collect(Collectors.toList());
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
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        System.out.println("Username: " + username);
        Long userId = (user != null) ? user.getUserId() : null;
        return courseService.getCourses(hourRange, minPrice, maxPrice, isPaid, starRating, categoryId, pageable, userId);
    }

    @GetMapping("/category/{categoryId}")
    public ResultPaginationDTO searchCoursesByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        System.out.println("Username: " + username);
        Long userId = (user != null) ? user.getUserId() : null;
        return courseService.getCoursesByCategory(categoryId, pageable, userId);
    }
}
