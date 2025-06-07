package com.elearning.haui.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.elearning.haui.domain.entity.OtpToken;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.repository.OtpTokenRepository;

@Service
public class OtpService {

    @Autowired
    OtpTokenRepository otpTokenRepository;
    @Autowired
    EmailService emailService;

    public String generateOtp() {
    int otp = 100000 + new Random().nextInt(900000); // 6 chữ số
    return String.valueOf(otp);
}

public void sendOtpEmail(User user, String type) {
   // long start = System.currentTimeMillis();
    int MAX_OTP_PER_DAY = 5;
    int OTP_COOLDOWN_SECONDS = 60;  
    
    // Kiểm tra số lần gửi OTP trong ngày
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

    int sentToday = otpTokenRepository.countOtpSentToday(user.getUserId(), type, startOfDay, endOfDay);

    if (sentToday >= MAX_OTP_PER_DAY) {
        throw new RuntimeException("You have sent too many OTPs for " + type + " in a day.");
    }

    
   Optional<OtpToken> lastOtpOptional = otpTokenRepository.findFirstByUserUserIdAndTypeOrderByCreatedAtDesc(user.getUserId(), type);


    if (lastOtpOptional.isPresent()) {
    OtpToken lastOtp = lastOtpOptional.get();

    if (lastOtp.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(OTP_COOLDOWN_SECONDS))) {
        throw new RuntimeException("Please wait at least " + OTP_COOLDOWN_SECONDS + " seconds.");
    }
}

    // create otp
    String otp = generateOtp();
    OtpToken token = new OtpToken();
    token.setUser(user);
    token.setOtpCode(otp);
    token.setExpiresAt(LocalDateTime.now().plusMinutes(5));  
    token.setVerified(false);
    token.setType(type);
    token.setCreatedAt(LocalDateTime.now());

    otpTokenRepository.save(token);

    // send mail
    String subject = type.equals("REGISTER") ? "Xác thực đăng ký" : "Khôi phục mật khẩu";
    String body = "Mã OTP của bạn là: " + otp;
    emailService.send(user.getEmail(), subject, body);
    // long end = System.currentTimeMillis();
    // System.out.println(" Thời gian gửi OTP qua email: " + (end - start) + "ms");
}



}
