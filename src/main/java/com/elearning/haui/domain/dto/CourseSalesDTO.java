package com.elearning.haui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseSalesDTO {
    private Long courseId;
    private String courseName;
    private String categoryName;
    private Double price;
    private Long quantitySold;
    private Double totalSales;
    private String author;

}
