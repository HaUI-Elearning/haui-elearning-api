package com.elearning.haui.api;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.smartcardio.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.config.VNPayConfig;
import com.elearning.haui.domain.entity.Cart;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Enrollment;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.entity.OrderDetail;
import com.elearning.haui.domain.entity.Payment;
import com.elearning.haui.domain.request.PaymentRequest;
import com.elearning.haui.repository.CartDetailRepository;
import com.elearning.haui.repository.CartRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.repository.EnrollmentRepository;
import com.elearning.haui.repository.OrderRepository;
import com.elearning.haui.repository.PaymentRepository;
import com.elearning.haui.service.CartService;
import com.elearning.haui.service.paymentsService;
import com.elearning.haui.utils.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentsAPI {
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartDetailRepository cartDetailRepository;
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    paymentsService paymentService;
    @Autowired
    VNPayConfig config;
    
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(Authentication authentication,@RequestBody PaymentRequest Paymentrequest, HttpServletRequest request) throws UnsupportedEncodingException {
        if(!Paymentrequest.isViaCart()&&Paymentrequest.getCourseIds().size()>1){
            throw new RuntimeException("Cannot purchase directly with more than 2 courses");
        }

        if(Paymentrequest.getCourseIds().isEmpty()){
            throw new RuntimeException("Payment cannot be made for course quantity <1");
        }
        Order order = paymentService.createOrder(authentication.getName(),Paymentrequest.getCourseIds(),Paymentrequest.isViaCart());

        String txnRef = VNPayUtil.generateRandomTxnRef();
        Payment payment = paymentService.createPayment(order, txnRef);
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", String.valueOf((long)(order.getTotalAmount() * 100)));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + txnRef);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", config.getReturnUrl());
        params.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("vnp_CreateDate", vnp_CreateDate);

        // Tạo chuỗi hash
        String queryString = VNPayUtil.buildHashData(params);
        String secureHash = VNPayUtil.hmacSHA512(config.getHashSecret(), queryString);
        queryString += "&vnp_SecureHash=" + secureHash;

        String paymentUrl = config.getPayUrl() + "?" + queryString;
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @GetMapping("/vnpay-return")
    @Transactional
    public ResponseEntity<?> handleReturn(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> fields = VNPayUtil.getFieldsFromRequest(request);
        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        if (vnp_SecureHash == null || !vnp_SecureHash.equals(VNPayUtil.hmacSHA512(config.getHashSecret(), VNPayUtil.buildHashData(fields)))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        if (responseCode == null || txnRef == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing response code or transaction reference");
        }

        Payment payment = paymentRepository.findByTxnRef(txnRef);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not found");
        }

        Order order = payment.getOrder();
        if (order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found for this payment");
        }

        if ("00".equals(responseCode)) {
            paymentService.updatePaymentStatus(payment.getPaymentId(), "success", responseCode);
            order.setStatus("paid");
            orderRepository.save(order);

            // Delete cart if via cart
            if (order.isViaCart()) {
                Cart cart = cartRepository.findByUser(order.getUser()).orElse(null);
                if (cart != null && order.getOrderDetails() != null) {
                    List<Long> purchasedCourseIds = order.getOrderDetails().stream()
                            .map(detail -> detail.getCourse().getCourseId())
                            .collect(Collectors.toList());
                    cartService.removeCartDetailsByCartAndCourseIds(cart.getCartId(), purchasedCourseIds);
                }
            }

            // Open course for user
            List<Enrollment> listCourseEnroll = new ArrayList<>();
            for (OrderDetail detail : order.getOrderDetails()) {
                if (!enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(order.getUser().getUserId(), detail.getCourse().getCourseId())) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setUser(order.getUser());
                    enrollment.setCourse(detail.getCourse());
                    enrollment.setEnrollmentDate(LocalDateTime.now());
                    listCourseEnroll.add(enrollment);
                    courseRepository.updateSold(detail.getCourse().getCourseId());
                }
            }
            enrollmentRepository.saveAll(listCourseEnroll);
            return ResponseEntity.ok("Payment successful!");
        } else {
            paymentService.updatePaymentStatus(payment.getPaymentId(), "failed", responseCode);
            order.setStatus("failed");
            orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
        }
    }

}
