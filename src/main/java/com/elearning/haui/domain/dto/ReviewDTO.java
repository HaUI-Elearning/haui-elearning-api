package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private long reviewId;
    private Long courseId;
    private Long userId;
    private double rating;
    private String comment;
    private LocalDateTime createdAt ;
}
