package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.CartDTO;
import com.elearning.haui.domain.dto.CartDetailDTO;
import com.elearning.haui.domain.dto.CourseDTO;
import com.elearning.haui.domain.entity.Cart;
import com.elearning.haui.domain.entity.CartDetail;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.CartDetailRepository;
import com.elearning.haui.repository.CartRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartDetailRepository cartDetailRepository,
            CourseRepository courseRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public CourseDTO addCourseToCart(String username, Long courseId, int quantity) {
        // Lấy user từ username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found for username: " + username);
        }

        // Kiểm tra khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Kiểm tra giỏ hàng
        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null) {
            // Tạo giỏ hàng mới nếu chưa có
            cart = new Cart(user);
            cart.setQuantity(0);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart); // Lưu giỏ hàng mới vào cơ sở dữ liệu
        }

        // Khởi tạo danh sách nếu chưa có (không cần thiết nếu đã khởi tạo trong lớp
        // Cart)
        if (cart.getCartDetails() == null) {
            cart.setCartDetails(new ArrayList<>());
        }

        // Kiểm tra chi tiết giỏ hàng (CartDetail)
        CartDetail cartDetail = cartDetailRepository.findByCartAndCourse(cart, course).orElse(null);

        if (cartDetail != null) {
            throw new RuntimeException("Course already exists in the cart. Skipping addition.");
        } else {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setCourse(course);
            newCartDetail.setQuantity(quantity); // Cập nhật theo số lượng mong muốn
            newCartDetail.setPrice(course.getPrice()); // Lưu giá của khóa học

            cartDetailRepository.save(newCartDetail);
        }

        // Cập nhật lại tổng số lượng và tổng giá trị của Cart
        updateCart(cart);

        return new CourseDTO(
                course.getCourseId(),
                course.getName(),
                course.getThumbnail(),
                course.getDescription(),
                course.getContents(),
                course.getStar(),
                course.getHour(),
                course.getPrice(),
                course.getAuthor(),
                course.getChapters(),
                course.getCreatedAt());
    }

    public CartDTO getCartDetails(String username) {
        // Lấy thông tin người dùng từ username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // Kiểm tra nếu giỏ hàng chưa có cartDetails, khởi tạo danh sách rỗng
        if (cart.getCartDetails() == null) {
            cart.setCartDetails(new ArrayList<>());
        }

        // Lấy danh sách các CartDetail liên quan đến Cart
        List<CartDetailDTO> cartDetails = cart.getCartDetails().stream()
                .map(cd -> new CartDetailDTO(
                        cd.getCourse().getCourseId(),
                        cd.getCourse().getName(),
                        cd.getCourse().getThumbnail(),
                        cd.getQuantity(),
                        cd.getPrice()))
                .collect(Collectors.toList());

        // Trả về DTO chứa thông tin giỏ hàng và chi tiết giỏ hàng
        return new CartDTO(cart.getCartId(), cart.getQuantity(), cart.getTotalPrice(), cartDetails);
    }

    @Transactional
    public boolean deleteCourseInCart(String username, Long courseId) {
        // Lấy user từ username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Xử lý khi không tìm thấy user
            new RuntimeException("User not found for username: " + username);
        }

        // Tìm cart của user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // Xoá cartDetail
        if (cartDetailRepository.deleteByUserIdAndCourseId(user.getUserId(), courseId) > 0) {
            updateCart(cart);

            // Kiểm tra xem giỏ hàng còn món nào không
            if (cart.getCartDetails().isEmpty()) {
                // Nếu giỏ hàng không còn gì, có thể xóa Cart hoặc cập nhật trạng thái giỏ hàng
                cartRepository.delete(cart); // Nếu bạn muốn xóa giỏ hàng khi không còn sản phẩm
            }
            return true;
        }
        return false;
    }

    private void updateCart(Cart cart) {
        // Cập nhật lại tổng giá trị giỏ hàng sau khi xóa một CartDetail
        double totalPrice = 0;

        for (CartDetail cartDetail : cart.getCartDetails()) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cart.setQuantity(cart.getCartDetails().size());
        cartRepository.save(cart); // Lưu lại giỏ hàng đã được cập nhật
    }
}
