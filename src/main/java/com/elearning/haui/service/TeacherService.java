package com.elearning.haui.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.CourseCategory;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CategoryRepository;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.CourseCategoryRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class TeacherService {
    @Autowired
    CourseCategoryRepository courseCategoryRepository ;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CourseRepository courseRepository;
    private final LocalDateTime now=LocalDateTime.now();
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImgBBService imgBBService;

    //Map ListCourse to DTO
    public List<CourseDTO> mapperListCourseToDTO(List<Course> listCourse){
        List<CourseDTO> ListDTO=new ArrayList<>();
        for(Course c : listCourse){
            CourseDTO dto=new CourseDTO();
            dto.setAuthor(c.getAuthor().getName());
            List<ChaptersDTO> listChapterDTO=new ArrayList<>();
            for(Chapters chapter : c.getListChapters()){
                ChaptersDTO chaptersDTO=new ChaptersDTO();
                chaptersDTO.setId(chapter.getChapterId());
                chaptersDTO.setTitle(chapter.getTitle());
                chaptersDTO.setPosition(chapter.getPosition());
                chaptersDTO.setDescription(chapter.getDescription());
                chaptersDTO.setCreatedAt(chapter.getCreatedAt());
                listChapterDTO.add(chaptersDTO);
            }
            dto.setChapters(listChapterDTO);
            dto.setContents(c.getContents());
            dto.setCourseId(c.getCourseId());
            dto.setCreatedAt(c.getCreatedAt());
            dto.setDescription(c.getDescription());
            dto.setHour(c.getHour());
            dto.setName(c.getName());
            dto.setPrice(c.getPrice());
            dto.setStar(c.getStar());
            dto.setThumbnail(c.getThumbnail());
            ListDTO.add(dto);
        }
        return ListDTO;
    }
    //map course to DTO
    public CourseDTO mapCourseToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setAuthor(course.getAuthor().getName());

        List<ChaptersDTO> listChapterDTO = new ArrayList<>();
        for (Chapters chapter : course.getListChapters()) {
            ChaptersDTO chaptersDTO = new ChaptersDTO();
            chaptersDTO.setId(chapter.getChapterId());
            chaptersDTO.setTitle(chapter.getTitle());
            chaptersDTO.setPosition(chapter.getPosition());
            chaptersDTO.setDescription(chapter.getDescription());
            chaptersDTO.setCreatedAt(chapter.getCreatedAt());
            listChapterDTO.add(chaptersDTO);
        }
        dto.setChapters(listChapterDTO);

        dto.setContents(course.getContents());
        dto.setCourseId(course.getCourseId());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setDescription(course.getDescription());
        dto.setHour(course.getHour());
        dto.setName(course.getName());
        dto.setPrice(course.getPrice());
        dto.setStar(course.getStar());
        dto.setThumbnail(course.getThumbnail());
        
        return dto;
    }
    //get all list course created by Teacher
    public List<CourseDTO> getAllCourseByTeacher(String username){
        List<Course> listCourse=courseRepository.getAllCourseByTeacher(username);
        if (listCourse.isEmpty()) {
            throw new RuntimeException("No courses found for teacher ");
        }
        List<CourseDTO> listDTO=mapperListCourseToDTO(listCourse);; 
        return listDTO;
        
    }

    //get coure{id} created by Teacher 
    public CourseDTO getCoursById(String username,Long couseId){
        Course course =courseRepository.getCoursesIdByTeacher(username, couseId);
        if(course==null){
            throw new RuntimeException("not found course");
        }
        CourseDTO courseDTO= mapCourseToDTO(course);
        return courseDTO;
    }
    //Create Coure by Teacher
    public CourseDTO CreateCourseByTeacher(
        Long categoryId
       ,String username
       ,String content
       ,String Description
       ,String name
       ,Double price
       ,MultipartFile file
       ){
        Category category=categoryRepository.findByCategoryId(categoryId);
        if(category==null){
            throw new RuntimeException ("Category not found");
        }
        User user=userRepository.findByUsername(username);
        if(user==null){
            throw new RuntimeException("User not found");
        }
        System.out.println(user.getName());
        
        Course course=new Course();
        course.setAuthor(user);
        course.setContents(content);
        course.setCreatedAt(now);
        course.setDescription(Description);
        course.setHour(0.0);
        course.setName(name);
        course.setPrice(price);
        course.setStar(0.0);
        String imageUrl = imgBBService.checkAndUploadImage(file);
        if (imageUrl != null) {
            course.setThumbnail(imageUrl);
        }
        course.setSold(0);
        courseRepository.save(course);
        CourseCategory courseCategory=new CourseCategory();
        courseCategory.setCourse(course);
        courseCategory.setCategory(category);
        courseCategoryRepository.save(courseCategory);
        CourseDTO courseDTO= mapCourseToDTO(course);
        return courseDTO;
    }

    //Update Course by Teacher
    public CourseDTO UpdateCourseByTeacher(String username
       ,Long CourseId
       ,String content
       ,String Description
       ,String name
       ,Double price
       ,MultipartFile file,Long CategoryId){
            Course course =courseRepository.getCoursesIdByTeacher(username, CourseId);
            if(course==null){
                throw new RuntimeException("not found course");
            }
            Category category=categoryRepository.findByCategoryId(CategoryId);
            if(category==null){
                throw new RuntimeException ("Category not found");
            }
            course.setContents(content);
            course.setDescription(Description);
            course.setName(name);
            course.setPrice(price);
            if (file != null && !file.isEmpty()) {
            String imageUrl = imgBBService.checkAndUploadImage(file);
                if (imageUrl != null) {
                    course.setThumbnail(imageUrl);
                }
            }
            courseRepository.save(course);
            CourseCategory courseCategory=courseCategoryRepository.findByCourse(course);
            if(courseCategory ==null){
                throw new RuntimeException("not found course category");
            }
            courseCategory.setCategory(category);
            courseCategoryRepository.save(courseCategory);
            CourseDTO courseDTO= mapCourseToDTO(course);
            return courseDTO;
    }
    //Delete Course by Teacher
    public boolean deleteCoursByTeacher(String username,Long courseId){
        Course course =courseRepository.getCoursesIdByTeacher(username,courseId);
        if(course==null){
            throw new RuntimeException("not found course");
        }
        courseRepository.delete(course);
        return true;
    }
       
        
}
