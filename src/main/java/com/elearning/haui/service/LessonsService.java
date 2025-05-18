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
import com.elearning.haui.domain.entity.Lessons;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.LessonsRepository;
@Service
public class LessonsService {
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
        if(list.isEmpty()){
            throw new RuntimeException("Lessons is null");
        }
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
    //create Lesson
    @Transactional(rollbackFor = Exception.class)
public LessonsDTO createLessonsByTeacher(
        String username,
        Long chapterId,
        String title,
        MultipartFile videoFile,
        MultipartFile pdfFile) throws IOException {
    
    Chapters chapter = chaptersRepository.findById(chapterId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy Chapter"));

    Lessons lesson = new Lessons();
    lesson.setChapter(chapter);
    lesson.setTitle(title);
    lesson.setCreatedAt(LocalDateTime.now());

    int currentPosition = lessonsRepository.countLessonsByCourseAndAuthor(username, chapterId);
    lesson.setPosition(currentPosition);

    lesson = lessonsRepository.save(lesson);

    if (videoFile != null && !videoFile.isEmpty() && videoFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Kích thước video vượt quá giới hạn 100MB");
    }
    if (pdfFile != null && !pdfFile.isEmpty() && pdfFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Kích thước PDF vượt quá giới hạn 100MB");
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

        lesson = lessonsRepository.save(lesson);
        LessonsDTO dto = mapLessonToDTO(lesson);
        return dto;
    } catch (Exception e) {
        throw new IOException("Không thể tải file lên Cloudinary: " + e.getMessage(), e);
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
        .orElseThrow(() -> new RuntimeException("Không tìm thấy Chapter"));

    Lessons lesson = lessonsRepository.getLessonsById(username, chapterId, lessonId);
    if (lesson == null) {
        throw new RuntimeException("Không tìm thấy lesson");
    }

    lesson.setTitle(title);
    lesson.setPosition(position);

    if (videoFile != null && !videoFile.isEmpty() && videoFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Kích thước video vượt quá giới hạn 100MB");
    }
    if (pdfFile != null && !pdfFile.isEmpty() && pdfFile.getSize() > 100 * 1024 * 1024) {
        throw new IOException("Kích thước PDF vượt quá giới hạn 100MB");
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

        if (videoResult != null) {
            System.out.println("Thời gian video: " + videoResult.getDuration());
        } else {
            System.out.println("videoResult is null");
        }

        lesson = lessonsRepository.save(lesson);
        LessonsDTO dto = mapLessonToDTO(lesson);
        return dto;
    } catch (Exception e) {
        throw new IOException("Không thể tải file lên Cloudinary: " + e.getMessage(), e);
    }
}

    //delete Lesson by teacher
    public boolean deleteLesson(String username,Long chapterId,Long lessonId){
        Lessons lesson = lessonsRepository.getLessonsById(username, chapterId, lessonId);
        if(lesson==null){
            throw new RuntimeException("Not found lesson");
        }
        lessonsRepository.delete(lesson);
        return true;
    }
}
