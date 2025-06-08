package com.elearning.haui.api;

import java.io.IOException;
import java.util.List;

import org.eclipse.tags.shaded.org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.LessonsDTO;
import com.elearning.haui.domain.dto.TeacherCourseDTO;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.LessonsRepository;
import com.elearning.haui.service.ChaptersService;
import com.elearning.haui.service.LessonsService;
import com.elearning.haui.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/v1/Teacher")
public class TeacherAPI {

    @Autowired
    LessonsRepository lessonsRepository;

    @Autowired
    ChaptersRepository chaptersRepository;

    @Autowired
    TeacherService teacherService;

    @Autowired 
    ChaptersService chaptersService;

    @Autowired
    LessonsService lessonsService;

    //Courses
    //Get course by Teacher
    @Operation(summary = "Lấy tất cả khóa học do giáo viên tạo")
    @GetMapping("/getAll/Courses")
    public ResponseEntity<?> getAllCourses(Authentication authentication){
        List<?> result=teacherService.getAllCourseByTeacher(authentication.getName());
        return ResponseEntity.ok(result);
    }
    //get course id by teacher
    @Operation(summary = "Lấy Khóa học theo id bởi giáo viên")
    @GetMapping("/getCourse/{courseId}")
    public ResponseEntity<?> getCouseByID(Authentication authentication,@PathVariable("courseId") Long courseId){
        TeacherCourseDTO result=teacherService.getCoursById(authentication.getName(), courseId);
        return ResponseEntity.ok(result);
    }
    //Create Course by teacher
    @Operation(summary = "Tạo khóa học bởi giáo viên")
    @PostMapping(value = "/Course/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> CreateCourse(Authentication authentication
       ,@RequestParam String content
       ,@RequestParam String Description
       ,@RequestParam String name
       ,@RequestParam Double price
       ,@RequestPart MultipartFile file,
       @RequestParam Long categoryId)
    {
        TeacherCourseDTO result=teacherService.CreateCourseByTeacher(categoryId,authentication.getName(), content, Description, name, price, file);
        return ResponseEntity.ok(result);
    }
    //Update Course By Teacher
    @Operation(summary = "Cập nhật khóa học bởi giáo viên")
    @PutMapping(value = "/Course/Update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> UpdateCourse(Authentication authentication
       ,@RequestParam Long CourseId
       ,@RequestParam String content
       ,@RequestParam String Description
       ,@RequestParam String name
       ,@RequestParam Double price
       ,@RequestPart(value = ("file"),required = false) MultipartFile file
       ,@RequestParam Long CategoryId
    )
    {
        TeacherCourseDTO result=teacherService.UpdateCourseByTeacher(authentication.getName(), CourseId, content, Description, name, price, file,CategoryId);
        return ResponseEntity.ok(result);
    }
    //Delete Course By teacher
    @Operation(summary = "Xóa khóa học bởi giáo viên")
    @DeleteMapping("/Course/delete/{CourseId}")
    public ResponseEntity<?> deleteCourse(Authentication authentication,@PathVariable("CourseId")Long CourseId)
    {
        boolean result =teacherService.deleteCoursByTeacher(authentication.getName(), CourseId);
        return ResponseEntity.ok(result);
    }

    
    //Chapters
    
    //get all chapters
    @Operation(summary = "Lấy tất cả chapters thuộc về khóa học theo id khóa học bởi giáo viên")
    @GetMapping("/getAll/{CourseId}/Chapters")
    public ResponseEntity<?> getAllChapter(Authentication authentication,@PathVariable("CourseId")Long CourseId)
    {
            List<ChaptersDTO> result=chaptersService.getAllChapters(authentication.getName(), CourseId);
            return ResponseEntity.ok(result);
    }
    //get Chapters By id
     @Operation(summary = "Lấy chapter theo idChapter thuộc về khóa học theo id khóa học bởi giáo viên")
    @GetMapping("/getChapter/{CourseId}/{ChapterId}")
    public ResponseEntity<?> getChapterById(Authentication authentication
    ,@PathVariable("CourseId")Long CourseId
    ,@PathVariable("ChapterId") Long ChapterId)
    {
            ChaptersDTO result=chaptersService.getChapterByid(authentication.getName(), CourseId, ChapterId);
            return ResponseEntity.ok(result);
    }

    //Create chappter
    @Operation(summary = "Tạo chapters thuộc về khóa học theo id khóa học bởi giáo viên")
    @PostMapping("/{CourseId}/Chapter/add")
    public ResponseEntity<?> createChapter(Authentication authentication,
                                        @PathVariable("CourseId") Long courseId,
                                        @RequestParam String title,
                                        @RequestParam String description,
                                        @RequestParam(required = false) Integer position) {
        int assignedPosition = (position == null) ? chaptersRepository.countChaptersByCourseAndAuthor(courseId, authentication.getName()) + 1 : position;
        ChaptersDTO result = chaptersService.addByTeacher(authentication.getName(), courseId, title, description, assignedPosition);
        return ResponseEntity.ok(result);
    }

    //Update chapter
    @Operation(summary = "Sửa chapters thuộc về khóa học theo id khóa học bởi giáo viên")
    @PutMapping("/{CourseId}/Chapter/update")
    public ResponseEntity<?> UpdateChapter(Authentication authentication
    ,@PathVariable("CourseId")Long CourseId
    ,@RequestParam Long ChapterId
    ,@RequestParam String title 
    ,@RequestParam String description
    ,@RequestParam int position
    )
    {
        ChaptersDTO result=chaptersService.updateByTeacher(authentication.getName(), CourseId, ChapterId, title, description, position);
        return ResponseEntity.ok(result);
    }
    //delete chapter
    @Operation(summary = "Xóa chapters thuộc về khóa học theo id khóa học bởi giáo viên")
    @DeleteMapping("/{CourseId}/Chapter/delete/{ChapterId}")
    public ResponseEntity<?> deleteChapter(Authentication authentication,
    @PathVariable("CourseId") Long CourseId
    ,@PathVariable("ChapterId") Long ChapterId)
    {
        boolean result=chaptersService.deleteByTeacher(authentication.getName(), CourseId, ChapterId);
        return ResponseEntity.ok(result);
    }
    //Lessons

    //Get all Lessons
    @Operation(summary = "Lấy tất cả Lessons thuộc chapter theo idChapter bởi giáo viên")
    @GetMapping("/getAll/Lesson/{ChapterId}")
    public ResponseEntity<?> getAllLesson(Authentication authentication,
    @PathVariable("ChapterId") Long ChapterId
    )
    {
        List<LessonsDTO> result=lessonsService.getAllLessonByTeacher(authentication.getName(), ChapterId);
        return ResponseEntity.ok(result);
    }

    //Get Lesson by Id
    @Operation(summary = "Lấy Lesson theo id thuộc chapter theo idChapter bởi giáo viên")
    @GetMapping("/getlesson/{ChapterId}/{LessonId}")
    public ResponseEntity<?> getLesson(Authentication authentication
    ,@PathVariable("ChapterId") Long ChapterId
    ,@PathVariable("LessonId") Long LessonId
    )
    {
        LessonsDTO  result=lessonsService.getLessonById(authentication.getName(), ChapterId, LessonId);
        return ResponseEntity.ok(result);
    }

    //Create Lesson by Teacher
    @Operation(summary = "Tạo Lesson thuộc chapter theo idChapter bởi giáo viên")
   @PostMapping(value = "/Lesson/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> CreateLesson(
            Authentication authentication,
            @RequestParam Long chapterId,
            @RequestParam String title,
            @RequestPart(value = "videoFile", required = false) 
            @Schema(type = "string", format = "binary") MultipartFile videoFile,
            @RequestPart(value = "pdfFile", required = false) 
            @Schema(type = "string", format = "binary") MultipartFile pdfFile,
            @RequestParam(required = false) Integer Position) throws IOException {
        int assignedPosition = (Position == null) ? lessonsRepository.countLessonsByCourseAndAuthor(authentication.getName(), chapterId) + 1 : Position;
        LessonsDTO result = lessonsService.createLessonsByTeacher(authentication.getName(), chapterId, title, videoFile, pdfFile, assignedPosition);
        return ResponseEntity.ok(result);
    }
    //Update Lesson
    @Operation(summary = "Sửa Lesson thuộc chapter theo idChapter bởi giáo viên")
    @PutMapping(value = "/Lesson/update/{lessonId}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> UpdateLesson(Authentication authentication,
    @RequestParam Long chapterId,
    @PathVariable("lessonId") Long lessonId,
    @RequestParam String title,
    @RequestParam int Position,
    @RequestPart(value = "videoFile", required = false) 
    @Schema(type = "string", format = "binary") MultipartFile videoFile,
    @RequestPart(value = "pdfFile", required = false) 
    @Schema(type = "string", format = "binary") MultipartFile pdfFile) throws IOException
    {
        LessonsDTO result=lessonsService.updateLessonsByTeacher(authentication.getName(), chapterId, lessonId, title, Position, videoFile, pdfFile);
        return ResponseEntity.ok(result);
    }
    //Delete Lesson
    @Operation(summary = "Xóa Lesson thuộc chapter theo idChapter bởi giáo viên")
    @DeleteMapping("/Lesson/delete/{lessonId}")
    public ResponseEntity<?> DeleteLesson(Authentication authentication,
    @RequestParam Long chapterId,
    @PathVariable("lessonId") Long lessonId)
    {
        boolean result=lessonsService.deleteLesson(authentication.getName(), chapterId, lessonId);
        return ResponseEntity.ok(result);
    }

    //get participants
    @Operation(summary = "Lấy danh sách người tham gia khóa học")
    @GetMapping("/getParticipants/{CourseId}")
    public ResponseEntity<?> getParticipants(Authentication authentication,@PathVariable("CourseId") Long CourseId)
    {
        return ResponseEntity.ok(teacherService.getParticipantsByCourseId(authentication.getName(), CourseId));
    }

    //View revenue by course
    @Operation(summary = "Xem doanh thu các khóa học của giáo viên")
    @GetMapping("/getRevenue/Courses")
    public ResponseEntity<?> getRevenueByCourse(Authentication authentication)
    {
        return ResponseEntity.ok(teacherService.getCourseRevenueByTeacher(authentication.getName()));
    }  
    
    //get course statistics by Teacher
    @Operation(summary = "Thống kê các khóa học của giáo viên")
    @GetMapping("/getStatistics/Courses")
    public ResponseEntity<?> getStatisticsCourse(Authentication authentication)
    {
        return ResponseEntity.ok(teacherService.getMonthlyGrowthForTeacher(authentication.getName()));
    }  

}
