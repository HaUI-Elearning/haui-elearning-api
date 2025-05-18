package com.elearning.haui.api;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.LessonsDTO;
import com.elearning.haui.service.ChaptersService;
import com.elearning.haui.service.LessonsService;
import com.elearning.haui.service.TeacherService;

@RestController
@RequestMapping("/api/v1/Teacher")
public class TeacherAPI {

    @Autowired
    TeacherService teacherService;

    @Autowired 
    ChaptersService chaptersService;

    @Autowired
    LessonsService lessonsService;

    //Courses
    //Get course by Teacher
    @GetMapping("/getAll/Courses")
    public ResponseEntity<?> getAllCourses(Authentication authentication){
        List<?> result=teacherService.getAllCourseByTeacher(authentication.getName());
        return ResponseEntity.ok(result);
    }
    //get course id by teacher
    @GetMapping("/getCourse/{courseId}")
    public ResponseEntity<?> getCouseByID(Authentication authentication,@PathVariable("courseId") Long courseId){
        CourseDTO result=teacherService.getCoursById(authentication.getName(), courseId);
        return ResponseEntity.ok(result);
    }
    //Create Course by teacher
    @PostMapping("/Course/add")
    public ResponseEntity<?> CreateCourse(Authentication authentication
       ,@RequestParam String content
       ,@RequestParam String Description
       ,@RequestParam String name
       ,@RequestParam Double price
       ,@RequestParam MultipartFile file)
    {
        CourseDTO result=teacherService.CreateCourseByTeacher(authentication.getName(), content, Description, name, price, file);
        return ResponseEntity.ok(result);
    }
    //Update Course By Teacher
    @PutMapping("/Course/Update")
    public ResponseEntity<?> UpdateCourse(Authentication authentication
       ,@RequestParam Long CourseId
       ,@RequestParam String content
       ,@RequestParam String Description
       ,@RequestParam String name
       ,@RequestParam Double price
       ,@RequestParam MultipartFile file
    )
    {
        CourseDTO result=teacherService.UpdateCourseByTeacher(authentication.getName(), CourseId, content, Description, name, price, file);
        return ResponseEntity.ok(result);
    }
    //Delete Course By teacher
    @DeleteMapping("/Course/delete/{CourseId}")
    public ResponseEntity<?> deleteCourse(Authentication authentication,@PathVariable("CourseId")Long CourseId)
    {
        boolean result =teacherService.deleteCoursByTeacher(authentication.getName(), CourseId);
        return ResponseEntity.ok(result);
    }

    
    //Chapters
    
    //get all chapters
    @GetMapping("/getAll/{CourseId}/Chapters")
    public ResponseEntity<?> getAllChapter(Authentication authentication,@PathVariable("CourseId")Long CourseId)
    {
            List<ChaptersDTO> result=chaptersService.getAllChapters(authentication.getName(), CourseId);
            return ResponseEntity.ok(result);
    }
    //get Chapters By id
    @GetMapping("/getChapter/{CourseId}/{ChapterId}")
    public ResponseEntity<?> getChapterById(Authentication authentication
    ,@PathVariable("CourseId")Long CourseId
    ,@PathVariable("ChapterId") Long ChapterId)
    {
            ChaptersDTO result=chaptersService.getChapterByid(authentication.getName(), CourseId, ChapterId);
            return ResponseEntity.ok(result);
    }

    //Create chappter
    @PostMapping("/{CourseId}/Chapter/add")
    public ResponseEntity<?> CreateChapter(Authentication authentication
    ,@RequestParam Long CourseId
    ,@RequestParam String title 
    ,@RequestParam String description)
    {
        ChaptersDTO result=chaptersService.addByTeacher(authentication.getName(), CourseId, title, description);
        return ResponseEntity.ok(result);
    }

    //Update chapter
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
    @GetMapping("/getAll/Lesson/{ChapterId}")
    public ResponseEntity<?> getAllLesson(Authentication authentication,
    @PathVariable("ChapterId") Long ChapterId
    )
    {
        List<LessonsDTO> result=lessonsService.getAllLessonByTeacher(authentication.getName(), ChapterId);
        return ResponseEntity.ok(result);
    }

    //Get Lesson by Id
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
    @PostMapping("/Lesson/add")
    public ResponseEntity<?> CreateLesson(
    Authentication authentication,
    @RequestParam  Long chapterId,
    @RequestParam  String title,
    @RequestParam  MultipartFile videoFile,
    @RequestParam  MultipartFile pdfFile
    ) throws IOException
    {
        LessonsDTO result=lessonsService.createLessonsByTeacher(authentication.getName(), chapterId, title, videoFile, pdfFile);
        return ResponseEntity.ok(result);
    }
    //Update Lesson
    @PutMapping("/Lesson/update/{lessonId}")
    public ResponseEntity<?> UpdateLesson(Authentication authentication,
    @RequestParam Long chapterId,
    @PathVariable("lessonId") Long lessonId,
    @RequestParam String title,
    @RequestParam int Position,
    @RequestParam MultipartFile videoFile,
    @RequestParam MultipartFile pdfFile) throws IOException
    {
        LessonsDTO result=lessonsService.updateLessonsByTeacher(authentication.getName(), chapterId, lessonId, title, Position, videoFile, pdfFile);
        return ResponseEntity.ok(result);
    }
    //Delete Lesson
    @DeleteMapping("/Lesson/delete/{lessonId}")
    public ResponseEntity<?> DeleteLesson(Authentication authentication,
    @RequestParam Long chapterId,
    @PathVariable("lessonId") Long lessonId)
    {
        boolean result=lessonsService.deleteLesson(authentication.getName(), chapterId, lessonId);
        return ResponseEntity.ok(result);
    }
}
