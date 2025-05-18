package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.Column;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonsDTO {
    private Long lessonId;
    private Long chapterId;
    private Long courseId;
    private String title;
    private String videoURL;
    private String pdfURL;
    private int position;
    private LocalDateTime createdAt;
    private Double durationVideo;
}
