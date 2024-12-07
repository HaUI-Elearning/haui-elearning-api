package com.elearning.haui.service;

import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.repository.FavoriteCourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteCourseService {

    private final FavoriteCourseRepository favoriteCourseRepository;

    public FavoriteCourseService(FavoriteCourseRepository favoriteCourseRepository) {
        this.favoriteCourseRepository = favoriteCourseRepository;
    }

    public FavoriteCourse addFavoriteCourse(FavoriteCourse favoriteCourse) {
        return favoriteCourseRepository.save(favoriteCourse);
    }

    public void removeFavoriteCourse(Long favoriteCourseId) {
        favoriteCourseRepository.deleteById(favoriteCourseId);
    }

    public List<FavoriteCourseDTO> getFavoriteCoursesByUserId(Long userId) {
        List<FavoriteCourse> favoriteCourses = favoriteCourseRepository.findByUser_UserId(userId);
        return favoriteCourses.stream()
                .map(fav -> new FavoriteCourseDTO(
                        fav.getId(),
                        fav.getUser().getUserId(),
                        fav.getCourse().getCourseId(),
                        fav.getCourse().getName(),
                        fav.getAddedAt()))

                .collect(Collectors.toList());
    }

}
