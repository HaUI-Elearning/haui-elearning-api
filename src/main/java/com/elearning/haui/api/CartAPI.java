package com.elearning.haui.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.service.CartService;
import com.elearning.haui.domain.dto.CartDTO;
import com.elearning.haui.domain.request.AddCourseToCartRequest;

@RestController
@RequestMapping("/api/v1/cart")
public class CartAPI {
    private final CartService cartService;

    public CartAPI(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("")
    public ResponseEntity<?> addToCart(@RequestBody AddCourseToCartRequest request)
            throws Exception {

        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        cartService.addCourseToCart(username, request.getCourseId(), request.getQuantity());
        return ResponseEntity.ok(null);

    }

    @GetMapping("")
    public ResponseEntity<CartDTO> getCart()
            throws Exception {

        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        return ResponseEntity.ok().body(cartService.getCartDetails(username));

    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCourseInCart(@RequestParam Long courseId)
            throws Exception {

        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Lấy username từ JWT

        if (!cartService.deleteCourseInCart(username, courseId)) {
            new Exception("Failed to delete course in cart for user");
        }
        return ResponseEntity.ok().body(null);

    }
}
