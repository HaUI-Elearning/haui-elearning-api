package com.elearning.haui.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.OtpToken;
@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken,Long> {
    @Query("""
            select o from OtpToken o
            where o.user.userId=:userId
            and o.otpCode=:otpCode 
            and o.type=:type
            and o.expiresAt > :currentTime and o.used = false 
     """)
OtpToken findValidOtp(
    @Param("userId") Long userId,
    @Param("otpCode") String otpCode,
    @Param("type") String type,
    @Param("currentTime") LocalDateTime currentTime
);


    @Query("""
        SELECT COUNT(o) FROM OtpToken o
        WHERE o.user.userId=:userId AND o.type = :type AND o.createdAt >= :startOfDay AND o.createdAt <= :endOfDay
         """)
int countOtpSentToday(@Param("userId") Long userId,
                      @Param("type") String type,
                      @Param("startOfDay") LocalDateTime startOfDay,
                      @Param("endOfDay") LocalDateTime endOfDay);

    Optional<OtpToken> findFirstByUserUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);

}
