package com.elearning.haui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDTO {
    private Long courseId;
    private String courseName;
    private String courseThumbnail;
    private Double price;
    private Double Star;
    private String author;
    private Double hourse;
}
