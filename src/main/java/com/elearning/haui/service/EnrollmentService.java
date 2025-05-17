package com.elearning.haui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.repository.EnrollmentRepository;;

@Service
public class EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;

  public EnrollmentService(EnrollmentRepository enrollmentRepository) {
    this.enrollmentRepository = enrollmentRepository;
  }

  public List<Enrollment> getEnrollmentByUserId(Long userId) {
    return this.enrollmentRepository.findByUser_UserId(userId);
  }

  // Ánh xạ từ Course sang CourseDTO
  private CourseDTO mapToCourseDTO(Course course) {
    //chapter
    List<ChaptersDTO> listChapterDTO=new ArrayList<>();
        for (Chapters c : course.getListChapters()) {
            ChaptersDTO chapterDTO=new ChaptersDTO();
            chapterDTO.setTitle(c.getTitle());
            chapterDTO.setDescription(c.getDescription());
            chapterDTO.setPosition(c.getPosition());
            chapterDTO.setCreatedAt(c.getCreatedAt());
            listChapterDTO.add(chapterDTO);

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
        course.getAuthor().getName(),
        listChapterDTO,
        course.getCreatedAt());
  }

  // Lấy danh sách các khóa học mà người dùng đã tham gia và ánh xạ sang CourseDTO
  public List<CourseDTO> getCoursesByUserId(Long userId) {
    // Lấy tất cả các Enrollment của người dùng theo userId
    List<Enrollment> enrollments = this.enrollmentRepository.findByUser_UserId(userId);

    // Ánh xạ từ Enrollment sang CourseDTO
    return enrollments.stream()
        .map(enrollment -> mapToCourseDTO(enrollment.getCourse())) // ánh xạ từ course trong enrollment
        .collect(Collectors.toList());
  }

}
