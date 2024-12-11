package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.CartDTO;
import com.elearning.haui.domain.dto.CartDetailDTO;
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

    public void addCourseToCart(String username, Long courseId, int quantity) {
        // Lấy user từ username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Xử lý khi không tìm thấy user
            new RuntimeException("User not found for username: " + username);
            return; // Hoặc tùy chọn khác: throw ngoại lệ tùy chỉnh, trả về lỗi
        }

        // Kiểm tra khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Kiểm tra giỏ hàng
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setQuantity(0);
                    newCart.setTotalPrice(0.0);
                    return cartRepository.save(newCart);
                });

        // Kiểm tra chi tiết giỏ hàng (CartDetail)
        CartDetail cartDetail = cartDetailRepository.findByCartAndCourse(cart, course).orElse(null);

        if (cartDetail != null) {
            // Nếu khóa học đã tồn tại trong giỏ hàng, bỏ qua
            new Exception("Course already exists in the cart. Skipping addition.");
        } else {
            // Tạo chi tiết giỏ hàng mới nếu chưa tồn tại
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setCourse(course);
            newCartDetail.setQuantity(1); // Mặc định số lượng là 1
            newCartDetail.setPrice(course.getPrice()); // Lưu giá của khóa học

            // Lưu thông tin chi tiết giỏ hàng
            cartDetailRepository.save(newCartDetail);
        }

        // Cập nhật lại tổng số lượng và tổng giá trị của Cart
        updateCart(cart);
    }

    public CartDTO getCartDetails(String username) {
        // Lấy thông tin người dùng từ username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            new RuntimeException("User not found");
        }

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Lấy danh sách các CartDetail liên quan đến Cart
        List<CartDetailDTO> cartDetails = cart.getCartDetails().stream()
                .map(cartDetail -> new CartDetailDTO(
                        cartDetail.getCourse().getCourseId(),
                        cartDetail.getCourse().getName(),
                        cartDetail.getCourse().getThumbnail(),
                        cartDetail.getQuantity(),
                        cartDetail.getPrice()))
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
