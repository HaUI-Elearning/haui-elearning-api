package com.elearning.haui.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.ReviewDTO;
import com.elearning.haui.domain.entity.Review;
import com.elearning.haui.domain.response.ReviewRestpone;
import com.elearning.haui.service.ReviewService;
@RestController
@RequestMapping("/api/v1/Review")
public class ReviewAPI {
    @Autowired
    ReviewService reviewService;
    // get all Review
    @GetMapping("/getAll/{CourseId}")
    public ResponseEntity<?> getAllReview(@PathVariable("CourseId") Long CourseId){
        ReviewRestpone rs=reviewService.getAllReview(CourseId);
        return ResponseEntity.ok(rs);
    }


    //get by id
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewByUser(Authentication authentication,@PathVariable("reviewId") Long reviewId)
    {
        ReviewDTO result=reviewService.getReviewByUser(authentication.getName(), reviewId);
        return ResponseEntity.ok(result);
    }

    //add review
    @PostMapping("/add")
    public ResponseEntity<?> addReview(Authentication authentication,
        @RequestParam Long CourseID,
        @RequestParam Double Rating,
        @RequestParam String Comment)
    {
        ReviewDTO rs=reviewService.addReviewByUser(authentication.getName(), CourseID, Rating, Comment);
        return ResponseEntity.ok(rs);
    }

    //update review
    @PutMapping("/update")
    public ResponseEntity<?> UpdateReview(Authentication authentication,
        @RequestParam  Long ReviewID,
        @RequestParam Double Rating,
        @RequestParam String Comment)
    {
        String rs=reviewService.UpdateReviewByUser(authentication.getName(),ReviewID, Rating, Comment);
        return ResponseEntity.ok(rs);
    }
    //delete review
    @DeleteMapping("/delete/{ReviewID}")
    public ResponseEntity<?> UpdateReview(Authentication authentication,
        @PathVariable ("ReviewID")  Long ReviewID
        )
    {
        String rs=reviewService.deleteReviewByUser(authentication.getName(),ReviewID);
        return ResponseEntity.ok(rs);
    }

    //filter review for course
    @GetMapping("/filter/{courseID}")
    public ResponseEntity<?> FilterReview(
        @PathVariable ("courseID") Long courseID
       ,@RequestParam int Stars
        )
    {
        if(Stars <1 || Stars>5){
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        List<?> rs=reviewService.filterReviewByStars(courseID, Stars);
        return ResponseEntity.ok(rs);
    }
}
