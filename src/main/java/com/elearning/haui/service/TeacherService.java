package com.elearning.haui.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseMonthlyGrowthDTO;
import com.elearning.haui.domain.dto.CourseRevenueDTO;
import com.elearning.haui.domain.dto.ParticipantsDTO;
import com.elearning.haui.domain.dto.TeacherCourseDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.CourseCategory;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.response.TotalRevenueTeacherRespone;
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
        dto.setApprovalStatus(c.getApprovalStatus());
        return dto;
    }
    //get all list course created by Teacher
    public List<TeacherCourseDTO> getAllCourseByTeacher(String username,String status){
        List<Course> listCourse=null;
        if(status == null)
        {
             listCourse=courseRepository.getAllCourseByTeacher(username);
        }
         
        else 
        {
            listCourse=courseRepository.getAllCourseByStatusForTeacher(username, status);
        }

        List<TeacherCourseDTO> listDTO=mapperListCourseToDTO(listCourse);; 
        return listDTO;
        
    }

    //get coure{id} created by Teacher 
    public TeacherCourseDTO getCoursById(String username,Long courseId){
        Course course =courseRepository.getCoursesIdByTeacher(username, courseId);
        if(course==null){
            throw new RuntimeException("not found course");
        }
        TeacherCourseDTO courseDTO= mapCourseToDTO(course);
        return courseDTO;
    }
    //get Notice of Rejection
    public String getRejectionReason(String username,Long courseId)
    {
        Course course =courseRepository.getCoursesIdByTeacher(username, courseId);
        if(!course.getApprovalStatus().equals("rejected"))
        {
            throw new RuntimeException("this course is not rejected");
        }
        else
            return course.getRejectionReason();
    }
    //send approval request
    public TeacherCourseDTO updateAndSendRequestApproval(String username
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
            if(!course.getApprovalStatus().equals("rejected"))
            {
                throw new RuntimeException("This course is not rejected ");
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
            course.setApprovalStatus("pending");
            course.setRejectionReason(null);
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
        course.setApprovalStatus("pending");
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
    //
    public TotalRevenueTeacherRespone getCourseRevenueByTeacher(String username) {
        TotalRevenueTeacherRespone respone=new TotalRevenueTeacherRespone();
        List<Course> courses = courseRepository.getAllCourseByTeacher(username);
        Double Total=0.0;
        List<CourseRevenueDTO> dtos = new ArrayList<>();
        for (Course c : courses) {
            if(c.getPrice()<=0||c.getApprovalStatus().equals("pending")||c.getApprovalStatus().equals("rejected"))
            {
                continue;
            }
            CourseRevenueDTO dto = new CourseRevenueDTO();
            dto.setCourseId(c.getCourseId());
            dto.setCourseName(c.getName());
            dto.setPrice(c.getPrice());
            dto.setStar(c.getStar());
            dto.setSold(c.getSold());
            dto.setRevenue(c.getPrice() * c.getSold());
            Total+=dto.getRevenue();
            dtos.add(dto);
        }
        respone.setTotalRevennue(Total);
        respone.setCourses(dtos);
        return respone;
    }

    //get 
    public List<CourseMonthlyGrowthDTO> getMonthlyGrowthForTeacher(String username) {
        LocalDateTime now = LocalDateTime.now();
        // ngày cuối cùng của tháng hiện tại và tháng trước
        LocalDateTime endOfCurrentMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime endOfPreviousMonth = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        // Lấy tổng học viên 
        List<Object[]> totalEnrollmentsCurrent = enrollmentRepository.countTotalEnrollmentsUpToDate(username, endOfCurrentMonth);
        List<Object[]> totalEnrollmentsPrevious = enrollmentRepository.countTotalEnrollmentsUpToDate(username, endOfPreviousMonth);
        
        // Lấy số học viên mới trong tháng này
        List<Object[]> newEnrollmentsThisMonth = enrollmentRepository.countEnrollmentsByCourseAndMonth(username, now.getMonthValue(), now.getYear());

        Map<Long, Integer> totalCurrentMap = new HashMap<>();
        totalEnrollmentsCurrent.forEach(r -> totalCurrentMap.put((Long) r[0], ((Number) r[1]).intValue()));

        Map<Long, Integer> totalPrevMap = new HashMap<>();
        totalEnrollmentsPrevious.forEach(r -> totalPrevMap.put((Long) r[0], ((Number) r[1]).intValue()));

        Map<Long, Integer> newStudentsMap = new HashMap<>();
        newEnrollmentsThisMonth.forEach(r -> newStudentsMap.put((Long) r[0], ((Number) r[1]).intValue()));

        List<Course> teacherCourses = courseRepository.getAllCourseByTeacher(username);
        List<CourseMonthlyGrowthDTO> result = new ArrayList<>();
        
        final int DAYS_THRESHOLD = 30; 

        for (Course course : teacherCourses) {
            Long courseId = course.getCourseId();
            
            int totalStudents = totalCurrentMap.getOrDefault(courseId, 0);
            boolean warning = false; 

            // check cảnh báo 
            if (course.getCreatedAt() != null) {
                long daysSinceCreation = java.time.temporal.ChronoUnit.DAYS.between(course.getCreatedAt(), LocalDateTime.now());
                
                if (daysSinceCreation > DAYS_THRESHOLD && totalStudents == 0) {
                    warning = true;
                }
            }

        int newStudents = newStudentsMap.getOrDefault(courseId, 0);
        int totalStudentsLastMonth = totalPrevMap.getOrDefault(courseId, 0);
        int growth = totalStudents - totalStudentsLastMonth;
        
        CourseMonthlyGrowthDTO dto = new CourseMonthlyGrowthDTO(
                courseId,
                course.getName(),
                now.getMonthValue(),
                now.getYear(),
                newStudents,
                totalStudentsLastMonth, 
                growth,
                warning 
        );
        result.add(dto);
    }

    return result;
    }
}
