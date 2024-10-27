package com.elearning.haui.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String courseTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status; // e.g., "active", "inactive"

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private Double price;

    private String currency;
}
