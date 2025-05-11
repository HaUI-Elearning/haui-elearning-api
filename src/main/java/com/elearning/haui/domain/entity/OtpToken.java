package com.elearning.haui.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp_tokens")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "otp_code")
    private String otpCode;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "verified")
    private boolean verified;
    @Column(name = "type")
    private String type; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "used")
    private boolean used;
}
