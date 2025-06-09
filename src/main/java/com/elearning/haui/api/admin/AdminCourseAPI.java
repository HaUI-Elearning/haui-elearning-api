package com.elearning.haui.api.admin;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.UserRepository;
import com.elearning.haui.service.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/Admin/courses")
public class AdminCourseAPI {

    private final CourseService courseService;

    public AdminCourseAPI(CourseService courseService) {
        this.courseService = courseService;
    }
    //confirm course by admin
    @Operation(summary = "Duyệt / từ chối khóa bởi admin")
    @PostMapping("/{id}/Confirm")
    public ResponseEntity<?> ConfirmCourse(@PathVariable("id") Long courseId,@RequestParam (required = false) String reason)
    {
        return ResponseEntity.ok(courseService.confirmCourse(courseId, reason));
    }


    //Get course by Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) throws RuntimeException {

        CourseDTO courseDTO = courseService.getCourseDetail(id);

        return ResponseEntity.ok(courseDTO);
    }

    //Get all course
    @Operation(summary = "Lấy danh sách khóa học phân trang,có thể lấy theo trạng thái Status,không truyền status mặc định get all")
    @GetMapping("")
    public ResponseEntity<ResultPaginationDTO> getAllCourses(
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional,
            @RequestParam(required=false) String Status
    ) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current -1, pageSize);
        Status= (Status != null) ? Status : null;
        ResultPaginationDTO result = courseService.fetchAllCourses(pageable,Status);

        return ResponseEntity.ok(result);
    }

    //Create course
    @PostMapping("")
    public ResponseEntity<?> createCourse(Authentication authentication
            ,@RequestParam String content
            , @RequestParam String description
            , @RequestParam String courseName
            , @RequestParam Double price
            , @RequestParam Double hour
            , @RequestParam MultipartFile file
            )
    {
        CourseDTO result = courseService.CreateCourse(authentication.getName(), content, description, courseName, price, hour,file);
        return ResponseEntity.ok(result);
    }

    //Update course
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id
            ,@RequestParam String author
            ,@RequestParam String content
            , @RequestParam String description
            , @RequestParam String courseName
            , @RequestParam Double price
            , @RequestParam Double hour
            , @RequestParam MultipartFile file
    ){
        CourseDTO result = courseService.updateCourse(id, author, content, description, courseName, price, hour,file);
        return ResponseEntity.ok(result);
    }

    //Delete course by id
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteCourse(@PathVariable Long id){
        courseService.deleteCourseById(id);
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage("Deleted course successfully");
        restResponse.setData(null);
        return ResponseEntity.ok(restResponse);
    }
}
