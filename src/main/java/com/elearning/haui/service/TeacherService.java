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
import com.elearning.haui.domain.dto.ParticipantsDTO;
import com.elearning.haui.domain.dto.TeacherCourseDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.CourseCategory;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CategoryRepository;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.CourseCategoryRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
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

    @Autowired
    EnrollmentRepository enrollmentRepository;

    //Map ListCourse to DTO
    public List<TeacherCourseDTO> mapperListCourseToDTO(List<Course> listCourse){
        List<TeacherCourseDTO> ListDTO = new ArrayList<>();
        for (Course c : listCourse) {
            ListDTO.add(mapCourseToDTO(c));
        }
        return ListDTO;
    }
    //map course to DTO
    public TeacherCourseDTO mapCourseToDTO(Course c) {
        CourseCategory courseCategory= courseCategoryRepository.findByCourse(c);
        if(courseCategory==null){
            throw new RuntimeException("This course does not currently belong to any category");
        }
        TeacherCourseDTO dto=new TeacherCourseDTO();
        dto.setCourseId(c.getCourseId());
        dto.setName(c.getName());
        dto.setContents(c.getContents());
        dto.setDescription(c.getDescription());
        dto.setPrice(c.getPrice());
        dto.setThumbnail(c.getThumbnail());
        dto.setStar(c.getStar());
        dto.setSold(c.getSold());
        dto.setHour(c.getHour());
        dto.setAuthor(c.getAuthor().getName());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setCategoryId(courseCategory.getCategory().getCategoryId());
        
        return dto;
    }
    //get all list course created by Teacher
    public List<TeacherCourseDTO> getAllCourseByTeacher(String username){
        List<Course> listCourse=courseRepository.getAllCourseByTeacher(username);
        if (listCourse.isEmpty()) {
            throw new RuntimeException("No courses found for teacher ");
        }
        List<TeacherCourseDTO> listDTO=mapperListCourseToDTO(listCourse);; 
        return listDTO;
        
    }

    //get coure{id} created by Teacher 
    public TeacherCourseDTO getCoursById(String username,Long couseId){
        Course course =courseRepository.getCoursesIdByTeacher(username, couseId);
        if(course==null){
            throw new RuntimeException("not found course");
        }
        TeacherCourseDTO courseDTO= mapCourseToDTO(course);
        return courseDTO;
    }
    //Create Coure by Teacher
    public TeacherCourseDTO CreateCourseByTeacher(
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
        TeacherCourseDTO courseDTO= mapCourseToDTO(course);
        return courseDTO;
    }

    //Update Course by Teacher
    public TeacherCourseDTO UpdateCourseByTeacher(String username
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
            TeacherCourseDTO courseDTO= mapCourseToDTO(course);
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
    
    //get participants
    public List<ParticipantsDTO> mapToDTO(List<Enrollment> users){
        List<ParticipantsDTO> dtos=new ArrayList<>();
        for(Enrollment u : users){
            ParticipantsDTO dto=new ParticipantsDTO();
            dto.setName(u.getUser().getName());
            dto.setJoinDate(u.getEnrollmentDate());
            dtos.add(dto);
        }
        return dtos;
    }
    public List<ParticipantsDTO> getParticipantsByCourseId(String username,Long CourseId){
        List<Enrollment> users=enrollmentRepository.getParticipantsByCourseId(username,CourseId);
        return mapToDTO(users);
    }
}
