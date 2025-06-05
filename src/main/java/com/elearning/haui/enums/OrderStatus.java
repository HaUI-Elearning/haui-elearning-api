package com.elearning.haui.enums;
import lombok.*;
@Getter

@AllArgsConstructor
public enum OrderStatus {
    PENDING("pending"),
    PAID("paid"), 
    FAILED("failed"),
    CANCELLED ("cancelled");
    private String value;
}