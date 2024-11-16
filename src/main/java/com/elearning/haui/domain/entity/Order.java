package com.elearning.haui.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

import com.elearning.haui.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;

    @Enumerated(EnumType.STRING) // Lưu giá trị Enum dưới dạng chuỗi
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // Quan hệ 1-1 với Payment: Mỗi Order có một Payment duy nhất
    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // Quan hệ 1-n với OrderDetail: Mỗi Order có nhiều OrderDetail
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails; // Sửa ở đây để lấy danh sách OrderDetai
}
