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

  @Column(nullable = false)
  private Double totalAmount;

  @Column(nullable = false)
  private LocalDateTime paymentDate = LocalDateTime.now();

  // 1-to-1 relation with Order
  @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Order order;

  private boolean status;
}
