package com.elearning.haui.domain.response;

import java.util.List;

import com.elearning.haui.domain.dto.ReviewDTO;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewResponse {
    Double avgRatting;
    List<ReviewDTO> listReview;
}
