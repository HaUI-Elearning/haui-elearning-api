package com.elearning.haui.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDate enrollmentDate;

    @Column(nullable = false)
    private String status; // e.g., "pending", "completed"
}
