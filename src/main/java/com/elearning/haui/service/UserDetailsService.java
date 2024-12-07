package com.elearning.haui.service;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.FavoriteCourseDTO;
import com.elearning.haui.domain.dto.UserDetailsDTO;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.User;

import java.util.stream.Collectors;

@Service
public class UserDetailsService {

    public UserDetailsDTO mapToUserDetailsDTO(User user) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserId(user.getUserId());
        userDetailsDTO.setUsername(user.getUsername());
        userDetailsDTO.setEmail(user.getEmail());
        userDetailsDTO.setName(user.getName());
        userDetailsDTO.setIntroduce(user.getIntroduce());
        userDetailsDTO.setCreatedAt(user.getCreatedAt());

        userDetailsDTO.setFavoriteCourses(
                user.getFavoriteCourses().stream()
                        .map(this::mapToFavoriteCourseDTO)
                        .collect(Collectors.toList()));

        return userDetailsDTO;
    }

    private FavoriteCourseDTO mapToFavoriteCourseDTO(FavoriteCourse favoriteCourse) {
        FavoriteCourseDTO favoriteCourseDTO = new FavoriteCourseDTO();
        favoriteCourseDTO.setId(favoriteCourse.getId());
        favoriteCourseDTO.setAddedAt(favoriteCourse.getAddedAt());
        return favoriteCourseDTO;
    }
}
