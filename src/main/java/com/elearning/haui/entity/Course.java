package com.elearning.haui.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String thumbnail;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column
    private String contents;

    @Column(nullable = false)
    private float star;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private double price;

    @Column(length = 100)
    private String author;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(name = "CourseCategories", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

}
