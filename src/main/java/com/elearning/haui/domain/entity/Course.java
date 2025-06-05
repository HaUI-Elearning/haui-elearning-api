package com.elearning.haui.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    

    @Column(name = "star")
    private Double star;

    @Column(nullable = false)
    private Double hour;

    @Column(nullable = false)
    private Double price;

    @Column(name = "sold")
    private int sold;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonManagedReference
    private User author;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Review> reviews;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FavoriteCourse> favoriteUsers;

    //
    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    private Set<Chapters> listChapters=new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
