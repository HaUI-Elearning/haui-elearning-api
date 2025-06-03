package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.Column;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChaptersDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    private int position;
    private List<LessonsDTO> listLessons;
}
