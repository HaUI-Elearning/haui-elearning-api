package com.elearning.haui.domain.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapters chapter;
    @Column(name="title")
    private String title;
    @Column(name = "video_url")
    private String videoUrl;
    @Column(name="pdf_url")
    private String pdfUrl;
    @Column(name = "position")
    private int position;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();


}
