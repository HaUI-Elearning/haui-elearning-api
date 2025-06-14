package com.elearning.haui.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Cart;
import com.elearning.haui.domain.entity.CartDetail;
import com.elearning.haui.domain.entity.Course;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    Optional<CartDetail> findByCartAndCourse(Cart cart, Course course);

    @Modifying
    @Query("DELETE FROM CartDetail cd WHERE cd.cart.user.id = :userId AND cd.course.id = :courseId")
    int deleteByUserIdAndCourseId(Long userId, Long courseId);
    @Modifying
    void deleteByCart_CartIdAndCourse_CourseIdIn(Long cartId, List<Long> courseIds);


    Optional<CartDetail> findByCourse_CourseIdAndCart_User_UserId(Long courseId, Long userId);

}
