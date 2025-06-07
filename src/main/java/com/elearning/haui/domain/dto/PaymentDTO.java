package com.elearning.haui.domain.dto;

import java.time.LocalDateTime;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private String status;
    private LocalDateTime paymentDate;
    private String txnRef;
    private Double totalAmount;
}
