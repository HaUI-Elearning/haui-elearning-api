package com.elearning.haui.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(nullable = false)
    private String reportType;

    private LocalDate generatedDate;

    @Column(columnDefinition = "TEXT")
    private String details;
}
