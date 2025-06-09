package com.elearning.haui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseRepone;
import com.elearning.haui.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentAPI {

    @Autowired
    private EnrollmentService enrollmentService;
    @Operation(summary="Lấy danh sách khóa học đã tham gia của User")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseRepone>> getCoursesUserIsEnrolledIn(@PathVariable Long userId) {
        List<CourseRepone> courses = enrollmentService.getCoursesByUserId(userId);
        return ResponseEntity.ok(courses);
    }
    @Operation(summary = "Ghi danh khóa học free")
    @PostMapping("/{CourseId}")
    public ResponseEntity<?> EnrollFreeCourse(Authentication authentication,@PathVariable("CourseId") Long CourseId){
        return ResponseEntity.ok(enrollmentService.EnrollFreeCourse(authentication.getName(), CourseId));
    }
}
