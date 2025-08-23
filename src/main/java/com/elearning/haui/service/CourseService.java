package com.elearning.haui.service;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.domain.dto.LessonsDTO;
import com.elearning.haui.domain.dto.Meta;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.ReviewDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Lessons;
import com.elearning.haui.domain.entity.Review;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CategoryRepository;
import com.elearning.haui.repository.CourseCategoryRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
import com.elearning.haui.repository.OrderDetailRepository;
import com.elearning.haui.repository.ReviewRepository;
import com.elearning.haui.repository.UserRepository;
import com.elearning.haui.utils.PaginationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LocalDateTime now = LocalDateTime.now();
    private final UserRepository userRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;
    ImgBBService imgBBService;
    @Autowired
    CourseCategoryRepository courseCategoryRepository;

    public CourseService(ImgBBService imgBBService,
                         OrderDetailRepository orderDetailRepository,
                         CourseCategoryRepository courseCategoryRepository,
                         CategoryRepository categoryRepository,
                         CourseRepository courseRepository, UserRepository userRepository) {
        this.imgBBService = imgBBService;
        this.orderDetailRepository = orderDetailRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void handleSaveProduct(Course course) {
        this.courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    public void deleteCourseById(Long id) {
        Course course = this.courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        this.courseRepository.delete(course);
    }

    public Course getCourseById(Long id) {
        return this.courseRepository.findCourseByCourseId(id);
    }

    public Page<Course> findPaginated(PageRequest pageRequest) {
        return this.courseRepository.findAll(pageRequest);
    }

    public ResultPaginationDTO fetchAllCourses(Pageable pageable,String Status) {
        Page<Course> pageCourses=null;
        if(Status==null)
        {
              pageCourses = this.courseRepository.findAllByAdmin(pageable);
        }
        else
        {
             pageCourses = this.courseRepository.findAllByStatus(Status,pageable);
        }
             
       
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageCourses.getNumber() + 1);
        meta.setTotal(pageCourses.getTotalElements());
        meta.setPages(pageCourses.getTotalPages());
        meta.setTotal(pageCourses.getTotalElements());

        rs.setMeta(meta);
        List<CourseDTO> courseDTOs = pageCourses.getContent().stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());

        rs.setResult(courseDTOs);
        return rs;
    }

    public CourseDTO convertToCourseDTO(Course course) {
        return convertToCourseDTO(course, null);
    }

    public CourseDTO convertToCourseDTO(Course course, Long userId) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        List<ChaptersDTO> listChapterDTO = new ArrayList<>();
        Set<Chapters> chapters = course.getListChapters();
        if (chapters != null) {
            for (Chapters c : chapters) {
                ChaptersDTO chapterDTO = new ChaptersDTO();
                chapterDTO.setTitle(c.getTitle());
                chapterDTO.setDescription(c.getDescription());
                chapterDTO.setPosition(c.getPosition());
                chapterDTO.setCreatedAt(c.getCreatedAt());

                List<LessonsDTO> listLessonDTO = new ArrayList<>();
                Set<Lessons> lessons = c.getListLessons();
                if (lessons != null) {
                    for (Lessons x : lessons) {
                        LessonsDTO dto = new LessonsDTO();
                        dto.setLessonId(x.getLessonId());
                        dto.setPosition(x.getPosition());
                        dto.setDurationVideo(x.getDuration());
                        dto.setCreatedAt(x.getCreatedAt());
                        dto.setTitle(x.getTitle());
                        dto.setChapterId(c.getChapterId());
                        dto.setCourseId(course.getCourseId());
                        listLessonDTO.add(dto);
                    }
                    listLessonDTO.sort(Comparator.comparing(LessonsDTO::getPosition));
                }
                chapterDTO.setListLessons(listLessonDTO);
                listChapterDTO.add(chapterDTO);
            }
            listChapterDTO.sort(Comparator.comparing(ChaptersDTO::getPosition));
        }

        boolean isEnrolled = false;
        boolean isAuthor = false;

        if (userId != null) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            isAuthor = checkAuthor(user.getName(), course);
            if (!isAuthor) {
                isEnrolled = enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(userId, course.getCourseId());
            }
        }

        return new CourseDTO(
            course.getCourseId(),
            course.getName(),
            course.getThumbnail(),
            course.getDescription(),
            course.getContents(),
            course.getStar(),
            course.getHour(),
            course.getPrice(),
            course.getSold(),
            (course.getAuthor() != null ? course.getAuthor().getName() : "Unknown"),
            listChapterDTO,
            course.getCreatedAt(),
            course.getApprovalStatus(),
            isEnrolled,
            isAuthor
        );
    }


    public long countCours() {
        return this.courseRepository.count();
    }

    public Map<String, Integer> getCourseCountByCategory() {
        List<Category> categories = categoryRepository.findAll();
        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (Category category : categories) {
            long count = courseCategoryRepository.countByCategory(category);
            categoryCountMap.put(category.getName(), (int) count);
        }
        return categoryCountMap;
    }

    // public List<CourseSalesDTO> getTopSellingCourses() {
    //     return orderDetailRepository.findTopSellingCourses();
    // }

    public ResultPaginationDTO getCoursesByCategory(Long categoryId, Pageable pageable, Long userId) {
        List<Course> allCourses = courseRepository.findCoursesByCategory(categoryId);
        return PaginationUtils.paginate(allCourses, pageable, course -> convertToCourseDTO(course, userId));
    }

    public List<CourseDTO> getCoursesByCategoryWithLimit(Long categoryId, Long userId) {
        List<Course> limitedCourses = courseRepository.findCoursesByCategory(categoryId);
        Collections.shuffle(limitedCourses);
        List<Course> randomCourses = limitedCourses.stream()
                .limit(10)
                .collect(Collectors.toList());
        return randomCourses.stream()
                .map(course -> convertToCourseDTO(course, userId))
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseDetail(Long courseId, Long userId) {
        Course course = courseRepository.findCourseByCourseId(courseId);
        if(course==null)
        {
            throw new RuntimeException("course is not approved");
        }
        return convertToCourseDTO(course, userId);
    }

    public CourseDTO getCourseDetailByAdmin(Long courseId) {
        return getCourseDetail(courseId, null);
    }

    public CourseDTO getCourseDetail(Long courseId) {
        Course course=courseRepository.findCourseByAdmin(courseId);
        return convertToCourseDTO(course);
    }

    public ResultPaginationDTO getCourses(String hourRange, Double minPrice, Double maxPrice, Boolean isPaid,
            String starRating, String categoryId, Pageable pageable, Long userId) {
        List<Course> allCourses = (categoryId == null) ? courseRepository.findAll() : courseRepository.findCoursesByCategory(Long.parseLong(categoryId));

        if (hourRange != null && !hourRange.isEmpty()) {
            allCourses = filterByHourRange(allCourses, hourRange);
        }
        if (minPrice != null && maxPrice != null) {
            allCourses = filterByPrice(allCourses, minPrice, maxPrice);
        }
        if (starRating != null) {
            allCourses = filterByStarRating(allCourses, starRating);
        }
        if (isPaid != null) {
            allCourses = filterByPaidStatus(allCourses, isPaid);
        }

        return PaginationUtils.paginate(allCourses, pageable, course -> convertToCourseDTO(course, userId));
    }

    private List<Course> filterByHourRange(List<Course> courses, String hourRange) {
        if (hourRange == null || hourRange.isEmpty())
            return courses;

        Set<Course> filterCourses = new HashSet<>();

        String[] hours = hourRange.split(",");
        for (String hour : hours) {
            switch (hour.trim()) {
                case "0-3":
                    filterCourses.addAll(courses.stream()
                            .filter(course -> course.getHour() >=0.1 && course.getHour() < 3)
                            .collect(Collectors.toList()));
                    break;
                case "3-6":
                    filterCourses.addAll(courses.stream()
                            .filter(course -> course.getHour() >= 3 && course.getHour() < 6)
                            .collect(Collectors.toList()));
                    break;
                case "6-9":
                    filterCourses.addAll(courses.stream()
                            .filter(course -> course.getHour() >= 6 && course.getHour() < 9)
                            .collect(Collectors.toList()));
                    break;
                case "9-12":
                    filterCourses.addAll(courses.stream()
                            .filter(course -> course.getHour() >= 9 && course.getHour() < 12)
                            .collect(Collectors.toList()));
                    break;
                case "Lâu hơn":
                    filterCourses.addAll(courses.stream()
                            .filter(course -> course.getHour() >= 12)
                            .collect(Collectors.toList()));
                    break;
            }
        }

        return new ArrayList<>(filterCourses);
    }

    private List<Course> filterByPrice(List<Course> courses, double minPrice, double maxPrice) {
        return courses.stream().filter(course -> course.getPrice() >= minPrice && course.getPrice() <= maxPrice).toList();
    }

    private List<Course> filterByStarRating(List<Course> courses, String starRating) {
        switch (starRating) {
            case "3": return courses.stream().filter(course -> course.getStar() >= 3).toList();
            case "4": return courses.stream().filter(course -> course.getStar() >= 4).toList();
            case "5": return courses.stream().filter(course -> course.getStar() == 5).toList();
            default: return courses;
        }
    }

    private List<Course> filterByPaidStatus(List<Course> courses, boolean isPaid) {
        return isPaid ? courses.stream().filter(course -> course.getPrice() > 0).toList() : courses.stream().filter(course -> course.getPrice() == 0).toList();
    }

    public CourseDTO CreateCourse(String username, String content, String description, String name, Double hour, Double price, MultipartFile file) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Course course = new Course();
        course.setAuthor(user);
        course.setContents(content);
        course.setCreatedAt(now);
        course.setDescription(description);
        course.setHour(hour);
        course.setName(name);
        course.setPrice(price);
        course.setStar(0.0);
        String imageUrl = imgBBService.checkAndUploadImage(file);
        if (imageUrl != null) {
            course.setThumbnail(imageUrl);
        }
        course.setSold(0);
        courseRepository.save(course);
        return convertToCourseDTO(course);
    }

    public CourseDTO updateCourse(long id, String courseName, String description, String content, String author, double hour, double price, MultipartFile file) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        course.setName(courseName);
        course.setDescription(description);
        course.setContents(content);
        course.setHour(hour);
        course.setPrice(price);
        User user = userRepository.findByName(author);
        if (user == null) {
            User userNew = new User();
            userNew.setUsername(author.replace(" ", ""));
            userNew.setName(author);
            userNew.setEmail(author.replace(" ", "") + "@gmail.com");
            userNew.setPassword("123456");
            userNew.setEmailVerified(true);
            user = userRepository.save(userNew);
        }
        course.setAuthor(user);
        if (file != null && !file.isEmpty()) {
            String imageUrl = imgBBService.checkAndUploadImage(file);
            if (imageUrl != null) {
                course.setThumbnail(imageUrl);
            }
        }
        courseRepository.save(course);
        return convertToCourseDTO(course);
    }

    //check Author
    public boolean checkAuthor(String name,Course course){
            if(!name.equals(course.getAuthor().getName())){
                return false;
            }
            return true;
    }

    //get detaill course by User
    public CourseDTO getCourseByUser(Long courseId,String username) 
    {
        Course course=courseRepository.findCourseByCourseId(courseId);
        if(course==null){
            throw new RuntimeException("Course not found");
        }
        User user=userRepository.findByUsername(username);
        if(user==null){
            throw new RuntimeException("User not found");
        }
        boolean isEnrolled=false;
        boolean isAuthor=checkAuthor(user.getName(), course);
        if(!isAuthor){
            isEnrolled=enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(user.getUserId(),courseId);
            if (!isEnrolled) {
                throw new RuntimeException("User has not enrolled in this course");
            }
        }
        
        
      
        List<ChaptersDTO> listChapterDTO = new ArrayList<>();
        Set<Chapters> chapters = course.getListChapters();
        if (chapters != null) {
            for (Chapters c : chapters) {
                ChaptersDTO chapterDTO = new ChaptersDTO();
                chapterDTO.setTitle(c.getTitle());
                chapterDTO.setDescription(c.getDescription());
                chapterDTO.setPosition(c.getPosition());
                chapterDTO.setCreatedAt(c.getCreatedAt());
                List<LessonsDTO> listLessonDTO = new ArrayList<>();
                Set<Lessons> lessons = c.getListLessons();
                if (lessons != null) {
                    for (Lessons x : lessons) {
                        LessonsDTO dto = new LessonsDTO();
                        dto.setPdfURL(x.getPdfUrl());
                        dto.setVideoURL(x.getVideoUrl());
                        dto.setLessonId(x.getLessonId());
                        dto.setPosition(x.getPosition());
                        dto.setDurationVideo(x.getDuration());
                        dto.setCreatedAt(x.getCreatedAt());
                        dto.setTitle(x.getTitle());
                        dto.setChapterId(c.getChapterId());
                        dto.setCourseId(course.getCourseId());
                        listLessonDTO.add(dto);
                    }
                    listLessonDTO.sort(Comparator.comparing(LessonsDTO::getPosition));
                }
                chapterDTO.setListLessons(listLessonDTO);
                listChapterDTO.add(chapterDTO);
            }
            listChapterDTO.sort(Comparator.comparing(ChaptersDTO::getPosition));
        }
        return new CourseDTO(
            course.getCourseId(),
            course.getName(),
            course.getThumbnail(),
            course.getDescription(),
            course.getContents(),
            course.getStar(),
            course.getHour(),
            course.getPrice(),
            course.getSold(),
            (course.getAuthor() != null ? course.getAuthor().getName() : "Unknown"),
            listChapterDTO,
            course.getCreatedAt(),
            course.getApprovalStatus(),
            isEnrolled,
            isAuthor
        );

    }

    //confirm course by admin
    public Object confirmCourse(Long courseId,String reason)
    {
        Course course=courseRepository.findCourseByAdmin(courseId);
        if(!(course!=null && course.getApprovalStatus().equals("pending")))
        {
            throw new RuntimeException("You can not approve this course ");
        }
        if(reason==null)
        {
            course.setApprovalStatus("approved");
            course.setApprovedAt(now);
        }
        else{
            course=courseRepository.findCourseByAdmin(courseId);
            course.setApprovalStatus("rejected");
            course.setRejectionReason(reason);
            course.setApprovedAt(now);
        }
        courseRepository.save(course);
        return "confirm Success";
    }
}