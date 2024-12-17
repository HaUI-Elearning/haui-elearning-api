package com.elearning.haui.api;

import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.service.CourseService;
import com.elearning.haui.service.FavoriteCourseService;
import com.elearning.haui.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite-courses")
public class FavoriteCourseAPI {

    private final FavoriteCourseService favoriteCourseService;
    private final UserService userService;
    private final CourseService courseService;

    public FavoriteCourseAPI(FavoriteCourseService favoriteCourseService, UserService userService,
            CourseService courseService) {
        this.favoriteCourseService = favoriteCourseService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteCourseDTO>> getFavorites() throws Exception {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        User user = userService.getUserByUsername(username);

        if (user == null) {
            new Exception("User not found");
        }

        return ResponseEntity.ok(favoriteCourseService.getFavoriteCoursesByUserId(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestParam Long courseId) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        User user = userService.getUserByUsername(username);

        if (user == null) {
            new Exception("User not found");
        }

        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            new Exception("Course not found");
        }

        favoriteCourseService.addFavoriteCourse(new FavoriteCourse(user, course));

        return ResponseEntity.ok(courseService.convertToCourseDTO(course));
    }

    @DeleteMapping
    public ResponseEntity<List<FavoriteCourseDTO>> removeFavorite(@RequestParam Long courseId) {

        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        User user = userService.getUserByUsername(username);

        if (user == null) {
            new Exception("User not found");
        }

        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            new Exception("Course not found");
        }

        favoriteCourseService.removeCourseInFavoriteCourse(user.getUserId(), course);

        return ResponseEntity.ok(favoriteCourseService.getFavoriteCoursesByUserId(user.getUserId()));
    }
}
