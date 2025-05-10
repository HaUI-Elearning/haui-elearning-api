package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.ReviewDTO;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Review;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.domain.response.ReviewRestpone;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.ReviewRepository;
import com.elearning.haui.repository.UserRepository;

@Service
public class ReviewService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired 
    UserRepository userRepository;

    //mapper review to DTO
    public List<?> mapperReviewDTO(List<Review> listReview,List<ReviewDTO> listReviewDTO){
        for(Review rv : listReview){
            ReviewDTO dto=new ReviewDTO(
            rv.getReviewId()
            ,rv.getCourse().getCourseId()
            ,rv.getUser().getUserId()
            ,rv.getRating(),
             rv.getComment(),
             rv.getCreatedAt());
             listReviewDTO.add(dto);
        }
        return listReviewDTO;
    }
     //check Rating and Comment
    private void validateRatingAndComment(Double Rating, String Comment) {
        if (Rating == null || Rating <= 0 || Rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (Comment == null || Comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }
    }

    //get All review
    public ReviewRestpone getAllReview(Long CourseId){
        Double aveRating=AVGRatting(CourseId);
        List<Review> list=reviewRepository.findReviewsByCourseId(CourseId);
        List<ReviewDTO> listDTO=new ArrayList<>();
        mapperReviewDTO(list,listDTO);
        ReviewRestpone rs=new ReviewRestpone(aveRating, listDTO);
        return rs;
    }

    //add review by User
    public String addReviewByUser(String Username,Long CourseID,Double Rating,String Comment){
        User user=userRepository.findByUsername(Username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + Username);
        }
        Course course=courseRepository.findById(CourseID).orElseThrow(()->new RuntimeException("Not found course"));
        validateRatingAndComment(Rating, Comment); 
        Review rv=new Review();
        rv.setUser(user);
        rv.setCourse(course);
        rv.setRating(Rating);
        rv.setComment(Comment);
        rv.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(rv);
        return "Add review success";

    }
    //update review
    public String UpdateReviewByUser(String Username,Long ReviewID,Double Rating,String Comment){
        User user=userRepository.findByUsername(Username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + Username);
        }
        
        validateRatingAndComment(Rating, Comment); 
        Review rv=reviewRepository.findById(ReviewID).orElseThrow(()->new RuntimeException("Not found Review id: "+ReviewID));
        if (!Objects.equals(rv.getUser().getUserId(), user.getUserId())) {
            throw new RuntimeException("User is not authorized to update this review.");
        }
        rv.setRating(Rating);
        rv.setComment(Comment);
        rv.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(rv);
        return "Update review success";

    }
    //delete review
    public String deleteReviewByUser(String username, Long reviewId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));
    
        if (!Objects.equals(review.getUser().getUserId(), user.getUserId())) {
                throw new RuntimeException("User is not authorized to delete this review.");
        }
        reviewRepository.delete(review);
        return "Delete review success";
    }

    //avg ratting
    public Double AVGRatting(Long CourseID){
        Double avg= reviewRepository.getAverageRatingByCourseId(CourseID);
        if (avg == null) return 0.0;
        return Math.round(avg * 10.0) / 10.0;
    }

    //filter review by stars
    public List<?> filterReviewByStars(Long CourseID,int levelStars)
    {
        int min=levelStars;
        int max=levelStars+1;
        List<Review> listReview=reviewRepository.findReviewsByRatingRangeForCourseId(min,max,CourseID);
        List<ReviewDTO> rs=new ArrayList<>();
        mapperReviewDTO(listReview,rs);
        return rs;
    }

}
