package com.elearning.haui.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

}
