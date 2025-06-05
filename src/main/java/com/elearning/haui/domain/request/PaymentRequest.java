package com.elearning.haui.domain.request;

import java.util.List;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    private List<Long> courseIds;
    private boolean viaCart;
}
