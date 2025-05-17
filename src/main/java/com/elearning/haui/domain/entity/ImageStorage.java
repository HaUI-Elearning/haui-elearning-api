package com.elearning.haui.domain.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="image_storage")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "hash",unique = true)
    private String hash;
    @Column(name = "url")
    private String url;
}
