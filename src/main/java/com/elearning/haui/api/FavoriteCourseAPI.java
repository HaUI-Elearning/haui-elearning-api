package com.elearning.haui.api;

import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.FavoriteCourseRepository;
import com.elearning.haui.service.CourseService;
import com.elearning.haui.service.FavoriteCourseService;
import com.elearning.haui.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite-courses")
public class FavoriteCourseAPI {
    @Autowired
    FavoriteCourseService favoriteCourseService;
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;

    @Autowired
    FavoriteCourseRepository favoriteCourseRepository;

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
        FavoriteCourse favoriteCourse= favoriteCourseRepository.findByCourseIdAndUserId(courseId,user.getUserId());
        if(favoriteCourse!=null){
            throw new RuntimeException("this course added your favorite list");
        }
        favoriteCourseService.addFavoriteCourse(new FavoriteCourse(user, course));

        return ResponseEntity.ok(favoriteCourseService.getFavoriteCoursesByUserId(user.getUserId()));
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
        FavoriteCourse favoriteCourse= favoriteCourseRepository.findByCourseIdAndUserId(courseId,user.getUserId());
        if(favoriteCourse==null){
            throw new RuntimeException("not found this course in your favourite list course");
        }
        favoriteCourseService.removeCourseInFavoriteCourse(user.getUserId(), course);

        return ResponseEntity.ok(favoriteCourseService.getFavoriteCoursesByUserId(user.getUserId()));
    }
}
