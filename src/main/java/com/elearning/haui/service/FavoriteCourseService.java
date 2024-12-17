package com.elearning.haui.service;

import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.User;
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

    public void removeCourseInFavoriteCourse(Long userId, Course course) {
        List<FavoriteCourse> favoriteCourses = getFavoriteCourseByUserIdToFavoriteCourse(userId);
        for (FavoriteCourse favoriteCourse : favoriteCourses) {
            if (favoriteCourse.getCourse() == course) {
                favoriteCourseRepository.delete(favoriteCourse);
                return;
            }
        }

    }

    public List<FavoriteCourse> getFavoriteCourseByUserIdToFavoriteCourse(Long userId) {
        return favoriteCourseRepository.findByUser_UserId(userId);
    }

    public List<FavoriteCourseDTO> getFavoriteCoursesByUserId(Long userId) {
        List<FavoriteCourse> favoriteCourses = favoriteCourseRepository.findByUser_UserId(userId);
        return favoriteCourses.stream()
                .map(fav -> new FavoriteCourseDTO(
                        fav.getId(),
                        fav.getCourse().getCourseId(),
                        fav.getCourse().getName(),
                        fav.getAddedAt()))

                .collect(Collectors.toList());
    }

}
