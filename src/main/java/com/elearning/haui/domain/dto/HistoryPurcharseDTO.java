package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryPurcharseDTO {
    private Long orderId;
    private LocalDateTime createdAt;
    private String orderStatus;
    private Double totalAmount;
    private List<CourseRepone> courses;
    private PaymentDTO payment;
}
