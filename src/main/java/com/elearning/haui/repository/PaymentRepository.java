package com.elearning.haui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.txnRef = :txnRef")
    Payment findByTxnRef(@Param("txnRef") String txnRef);

    @Query("""
        select p from Payment p
        join p.order o
        join o.user u
        where u.username = :username
        and p.status = :status
    """)
    List<Payment> findByUserIdAndStatus(@Param("username") String username, @Param("status") String status);
}
