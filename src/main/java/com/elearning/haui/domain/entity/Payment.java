package com.elearning.haui.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    // Enum status: 'success', 'failed', 'processing'
  
    @Column(name = "status")
    private String status;

    @Column(name = "txn_ref")
    private String txnRef;

    @Column(name = "response_code")
    private String responseCode;

    // Quan hệ 1-1 với Order: chủ sở hữu là Order
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
