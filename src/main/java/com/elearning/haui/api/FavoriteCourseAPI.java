package com.elearning.haui.api;

import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.service.FavoriteCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite-courses")
public class FavoriteCourseAPI {

    @Autowired
    private FavoriteCourseService favoriteCourseService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteCourseDTO>> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteCourseService.getFavoriteCoursesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<FavoriteCourse> addFavorite(@RequestBody FavoriteCourse favoriteCourse) {
        System.out.println(favoriteCourse.toString());
        return ResponseEntity.ok(favoriteCourseService.addFavoriteCourse(favoriteCourse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        favoriteCourseService.removeFavoriteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
