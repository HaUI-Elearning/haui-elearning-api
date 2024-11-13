package com.elearning.haui.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  @OneToMany(mappedBy = "payment")
  private List<PaymentDetail> paymentDetails;

  private boolean status;

}
