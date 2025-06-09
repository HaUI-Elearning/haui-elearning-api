package com.elearning.haui.service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.haui.domain.dto.LessonsDTO;
import com.elearning.haui.domain.dto.UploadResultDTO;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Lessons;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.LessonsRepository;
@Service
public class LessonsService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    LessonsRepository lessonsRepository;
    @Autowired
    ChaptersRepository chaptersRepository;

    @Autowired
    CloudinaryService cloudinaryService;
    //Map listLesson to DTO
    public List<LessonsDTO> mapperListLessonsToDTO(List<Lessons> list){
        List<LessonsDTO> listDTO=new ArrayList<>();
        for(Lessons l : list)
        {
            LessonsDTO dto=new LessonsDTO();
            dto.setChapterId(l.getChapter().getChapterId());
            dto.setCourseId(l.getChapter().getCourse().getCourseId());
            dto.setLessonId(l.getLessonId());
            dto.setTitle(l.getTitle());
            dto.setVideoURL(l.getVideoUrl());
            dto.setPdfURL(l.getPdfUrl());
            dto.setPosition(l.getPosition());
            dto.setDurationVideo(l.getDuration());
            dto.setCreatedAt(l.getCreatedAt());
            listDTO.add(dto);
        }
        return listDTO;
    }

    //Map Lessons To DTO
    public LessonsDTO mapLessonToDTO(Lessons l){
            LessonsDTO dto=new LessonsDTO();
            dto.setChapterId(l.getChapter().getChapterId());
            dto.setCourseId(l.getChapter().getCourse().getCourseId());
            dto.setLessonId(l.getLessonId());
            dto.setTitle(l.getTitle());
            dto.setVideoURL(l.getVideoUrl());
            dto.setPdfURL(l.getPdfUrl());
            dto.setPosition(l.getPosition());
            dto.setDurationVideo(l.getDuration());
            dto.setCreatedAt(l.getCreatedAt());
            return dto;
    }

    //get all
    public List<LessonsDTO> getAllLessonByTeacher(String username,Long chapterId){
        List<Lessons> list=lessonsRepository.getAllLessonsByChapterAndAuthor(chapterId, username);
        List<LessonsDTO> listDTO=mapperListLessonsToDTO(list);
        return listDTO;
    }
    //get by id
    public LessonsDTO getLessonById(String username,Long chapterId,Long LessonId){
        Lessons lesson=lessonsRepository.getLessonsById(username, chapterId, LessonId);
        if(lesson==null){
            throw new RuntimeException("Not found lesson");
        }
        LessonsDTO dto=mapLessonToDTO(lesson);
        return dto;
    }

    public void CaculalateHourseForCourse(Course course)
    {
        if(course==null)
        {
            return;
        }
        Double timeCourse =0.0;
        for(Chapters ct : course.getListChapters())
        {
            for(Lessons l : ct.getListLessons())
            {
              timeCourse += (l.getDuration() != null ? l.getDuration() : 0.0);
            }
        }
       
        Double hoursCourse = timeCourse / 3600;
        Double hour = Math.round(hoursCourse * 10) / 10.0;
        course.setHour(hour);
        courseRepository.save(course);
    }

    //create Lesson
    @Transactional(rollbackFor = Exception.class)
public LessonsDTO createLessonsByTeacher(
        String username,
        Long chapterId,
        String title,
        MultipartFile videoFile,
        MultipartFile pdfFile,int Position) throws IOException {
    
    Chapters chapter = chaptersRepository.findById(chapterId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy Chapter"));

    if (chapter.getCourse() == null || chapter.getCourse().getAuthor() == null
            || !username.equals(chapter.getCourse().getAuthor().getUsername())) {
        throw new IllegalStateException("Chapter not in teacher's course");
    }    

    // check position hợp lệ
    if (Position <= 0) {
        throw new IllegalArgumentException("Position must be greater than 0");
    }

    //get lessons hiện có trong chapter
    List<Lessons> existingLessons = lessonsRepository.findByChapterId(chapterId);
    int currentCount = existingLessons.size();
    if (Position > currentCount + 1) {
        throw new RuntimeException("Position cannot be greater than " + (currentCount + 1));
    }

    // sort old position
    for (Lessons existing : existingLessons) {
        if (existing.getPosition() >= Position) {
            existing.setPosition(existing.getPosition() + 1);
            lessonsRepository.save(existing);
        }
    }

    Lessons lesson = new Lessons();
    lesson.setChapter(chapter);
    lesson.setTitle(title);
    lesson.setCreatedAt(LocalDateTime.now());
    lesson.setPosition(Position);
    if ((videoFile == null || videoFile.isEmpty()) && (pdfFile == null || pdfFile.isEmpty())) {
        throw new IllegalArgumentException("At least one file (video or PDF) must be provided when creating a lesson.");
    }
    if (videoFile != null && !videoFile.isEmpty() && videoFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Video size exceeds 100MB limit");
    }
    if (pdfFile != null && !pdfFile.isEmpty() && pdfFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("PDF size exceeds 100MB limit");
    }
    
    // Tạo publicId cho video và PDF
    String slug = title.trim().toLowerCase().replaceAll("\\s+", "_");
    String videoPublicId = "lesson_" + chapterId + "_" + slug + "_" + username + "_video";
    String pdfPublicId = "lesson_" + chapterId + "_" + slug + "_" + username + "_pdf";
    // Đo thời gian bắt đầu
    long startTime = System.nanoTime();
    // Upload video và PDF song song
    CompletableFuture<UploadResultDTO> videoUploadFuture = (videoFile != null && !videoFile.isEmpty())
            ? cloudinaryService.uploadVideo(videoFile, videoPublicId)
            : CompletableFuture.completedFuture(null);

    CompletableFuture<UploadResultDTO> pdfUploadFuture = (pdfFile != null && !pdfFile.isEmpty())
            ? cloudinaryService.uploadPdf(pdfFile, pdfPublicId)
            : CompletableFuture.completedFuture(null);

    try {
        // Chờ cả hai upload hoàn thành
        CompletableFuture.allOf(videoUploadFuture, pdfUploadFuture).join();
        // Đo thời gian kết thúc và tính tổng thời gian
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Thoi gian tai file file: " + durationInMillis + " milliseconds");
        System.out.println("=================");
        UploadResultDTO videoResult = videoUploadFuture.get();
        UploadResultDTO pdfResult = pdfUploadFuture.get();

        lesson.setVideoUrl(videoResult != null ? videoResult.getUrl() : null);
        lesson.setPdfUrl(pdfResult != null ? pdfResult.getUrl() : null);
        lesson.setDuration(videoResult != null ? videoResult.getDuration() : null);

        if (videoResult != null) {
            System.out.println("Thời gian video: " + videoResult.getDuration());
        } else {
            System.out.println("videoResult is null");
        }
        Course course=courseRepository.findById(chapter.getCourse().getCourseId()).orElseThrow(()->new RuntimeException("Not found course"));
        CaculalateHourseForCourse(course);
        LessonsDTO dto = mapLessonToDTO(lesson);
        return dto;
    } catch (Exception e) {
        throw new IOException("can not upload to Cloudinary: " + e.getMessage(), e);
    }
}

    @Transactional(rollbackFor = Exception.class)
public LessonsDTO updateLessonsByTeacher(
        String username,
        Long chapterId,
        Long lessonId,
        String title,
        int position,
        MultipartFile videoFile,
        MultipartFile pdfFile) throws IOException {
    
    Chapters chapter = chaptersRepository.findById(chapterId)
        .orElseThrow(() -> new RuntimeException("Not found Chapter"));
    
    if (chapter.getCourse() == null || chapter.getCourse().getAuthor() == null
            || !username.equals(chapter.getCourse().getAuthor().getUsername())) {
        throw new IllegalStateException("Chapter not in teacher's course");
    }

    Lessons lesson = lessonsRepository.getLessonsById(username, chapter.getChapterId(), lessonId);
    if (lesson == null) {
        throw new RuntimeException("Not found lesson");
    }

    if (position <= 0) {
        throw new IllegalArgumentException("Position must be greater than 0");
    }

    List<Lessons> existingLessons = lessonsRepository.findByChapterId(chapterId);
    int currentCount = existingLessons.size();
    if (position > currentCount) {
        throw new RuntimeException("Position cannot be greater than " + currentCount);
    }
    //set old positions
    int oldPosition = lesson.getPosition();
    for (Lessons existing : existingLessons) {
        if (existing.getLessonId().equals(lessonId)) {
            continue;
        }
        if (oldPosition < position) {
            if (existing.getPosition() > oldPosition && existing.getPosition() <= position) {
                existing.setPosition(existing.getPosition() - 1);
                lessonsRepository.save(existing);
            }
        } else {
            if (existing.getPosition() >= position && existing.getPosition() < oldPosition) {
                existing.setPosition(existing.getPosition() + 1);
                lessonsRepository.save(existing);
            }
        }
    }
   

    if (videoFile != null && !videoFile.isEmpty() && videoFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Video size exceeds 100MB limit");
    }
    if (pdfFile != null && !pdfFile.isEmpty() && pdfFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("PDF size exceeds 100MB limit");
    }

    // Tạo publicId cho video và PDF
    String slug = title.trim().toLowerCase().replaceAll("\\s+", "_");
    String videoPublicId = "lesson_" + chapterId + "_" + slug + "_" + username + "_video";
    String pdfPublicId = "lesson_" + chapterId + "_" + slug + "_" + username + "_pdf";
    // Đo thời gian bắt đầu
    long startTime = System.nanoTime();
    // Upload video và PDF song song
    CompletableFuture<UploadResultDTO> videoUploadFuture = (videoFile != null && !videoFile.isEmpty())
            ? cloudinaryService.uploadVideo(videoFile, videoPublicId)
            : CompletableFuture.completedFuture(null);

    CompletableFuture<UploadResultDTO> pdfUploadFuture = (pdfFile != null && !pdfFile.isEmpty())
            ? cloudinaryService.uploadPdf(pdfFile, pdfPublicId)
            : CompletableFuture.completedFuture(null);

    try {
        // Chờ cả hai upload hoàn thành
        CompletableFuture.allOf(videoUploadFuture, pdfUploadFuture).join();
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Thoi gian tai file file: " + durationInMillis + " milliseconds");
        System.out.println("=================");
        UploadResultDTO videoResult = videoUploadFuture.get();
        UploadResultDTO pdfResult = pdfUploadFuture.get();
        lesson.setVideoUrl(videoResult != null ? videoResult.getUrl() : lesson.getVideoUrl());
        lesson.setPdfUrl(pdfResult != null ? pdfResult.getUrl() : lesson.getPdfUrl());
        lesson.setDuration(videoResult != null ? videoResult.getDuration() : lesson.getDuration());
        lesson.setPosition(position);
        lesson.setTitle(title);
        if (videoResult != null) {
            System.out.println("Thời gian video: " + videoResult.getDuration());
        } else {
            System.out.println("videoResult is null");
        }
        lessonsRepository.save(lesson);
        Course course=courseRepository.findById(chapter.getCourse().getCourseId()).orElseThrow(()->new RuntimeException("Not found course"));
        CaculalateHourseForCourse(course);
        LessonsDTO dto = mapLessonToDTO(lesson);
        return dto;
    } catch (Exception e) {
        throw new IOException("can not upload to Cloudinary: " + e.getMessage(), e);
    }
}

   @Transactional(rollbackFor = Exception.class)
public boolean deleteLesson(String username, Long chapterId, Long lessonId) {
    Lessons lesson = lessonsRepository.getLessonsById(username, chapterId, lessonId);
    if (lesson == null) {
        throw new RuntimeException("Not found lesson");
    }

    int deletedPosition = lesson.getPosition();
    lessonsRepository.delete(lesson);
    Chapters chapter=chaptersRepository.findById(chapterId).orElseThrow(()-> new RuntimeException("Chapter not found"));
    Course course=courseRepository.findById(chapter.getCourse().getCourseId()).orElseThrow(()->new RuntimeException("Not found course"));
    CaculalateHourseForCourse(course);

    // set olds position 
    List<Lessons> remainingLessons = lessonsRepository.findByChapterId(chapterId);
    for (Lessons remaining : remainingLessons) {
        if (remaining.getPosition() > deletedPosition) {
            remaining.setPosition(remaining.getPosition() - 1);
            lessonsRepository.save(remaining);
        }
    }

    return true;
}
}
