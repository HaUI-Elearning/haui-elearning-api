package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.CartDTO;
import com.elearning.haui.domain.dto.CartDetailDTO;
import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.dto.CourseRepone;
import com.elearning.haui.domain.entity.Cart;
import com.elearning.haui.domain.entity.CartDetail;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.FavoriteCourse;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CartDetailRepository;
import com.elearning.haui.repository.CartRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.FavoriteCourseRepository;
import com.elearning.haui.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    FavoriteCourseRepository favoriteCourseRepository;

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartDetailRepository cartDetailRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;
    public CartDTO mapperCartToDTO (Cart cart){
        CartDTO dto=new CartDTO();
        dto.setCartId(cart.getCartId());
        Double totalprice=0.0;
        int count=0;
        List<CartDetailDTO> listCartDetailDTO=new ArrayList<>();
        for(CartDetail detail : cart.getCartDetails()){
            CartDetailDTO cartDetailDTO=new CartDetailDTO();
            cartDetailDTO.setCourseId(detail.getCourse().getCourseId());
            cartDetailDTO.setCourseName(detail.getCourse().getName());
            cartDetailDTO.setCourseThumbnail(detail.getCourse().getThumbnail());
            cartDetailDTO.setPrice(detail.getCourse().getPrice());
            cartDetailDTO.setAuthor(detail.getCourse().getAuthor().getName());
            cartDetailDTO.setHourse(detail.getCourse().getHour());
            cartDetailDTO.setStar(detail.getCourse().getStar());
            listCartDetailDTO.add(cartDetailDTO);
            totalprice+=detail.getPrice();
            count++;
        }
        dto.setTotalPrice(totalprice);
        dto.setQuantity(count);
        dto.setCartDetails(listCartDetailDTO);
        return dto;

    }

    @Transactional 
    public CartDTO addCourseToCart(String username, Long courseId) {
       
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found for username: " + username);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getPrice() == 0) {
            throw new RuntimeException("Cannot add a free course to the cart.");
        }
        if (!"approved".equals(course.getApprovalStatus())) {
            throw new RuntimeException("This course is not approved.");
        }

        // 3. Lấy giỏ hàng hoặc tạo mới
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart; 
        });

        
        boolean courseExists = cart.getCartDetails().stream()
                .anyMatch(detail -> detail.getCourse().getCourseId().equals(courseId));

        if (courseExists) {
            throw new RuntimeException("Course already exists in the cart.");
        }

        // Tạo CartDetail mới
        CartDetail newCartDetail = new CartDetail();
        newCartDetail.setCourse(course);
        newCartDetail.setPrice(course.getPrice());
        
        newCartDetail.setCart(cart); 
        cart.getCartDetails().add(newCartDetail);

        cartRepository.save(cart);

        FavoriteCourse favoriteCourse = favoriteCourseRepository.findByCourseIdAndUserId(courseId, user.getUserId());
        if (favoriteCourse != null) {
            favoriteCourseRepository.delete(favoriteCourse);
        }

        return mapperCartToDTO(cart);
    }

    public CartDTO getCartDetails(String username) {
        // Lấy thông tin người dùng từ username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart; 
        });

        // Kiểm tra nếu giỏ hàng chưa có cartDetails, khởi tạo danh sách rỗng
        if (cart.getCartDetails() == null) {
            cart.setCartDetails(new ArrayList<>());
        }

        // Lấy danh sách các CartDetail liên quan đến Cart
         CartDTO dto= mapperCartToDTO(cart);
        return dto;
    }

    @Transactional
    public boolean deleteCourseInCart(String username, Long courseId) {
        // Lấy user từ username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Xử lý khi không tìm thấy user
            new RuntimeException("User not found for username: " + username);
        }
        // Xoá cartDetail
        if (cartDetailRepository.deleteByUserIdAndCourseId(user.getUserId(), courseId) > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public void removeCartDetailsByCartAndCourseIds(Long cartId, List<Long> courseIds) {
        cartDetailRepository.deleteByCart_CartIdAndCourse_CourseIdIn(cartId, courseIds);
    }

}
