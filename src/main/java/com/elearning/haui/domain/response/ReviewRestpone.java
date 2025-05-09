package com.elearning.haui.domain.response;

import java.util.List;

import com.elearning.haui.domain.dto.ReviewDTO;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRestpone {
    Double avgRatting;
    List<ReviewDTO> listReview;
}
