package com.elearning.haui.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.enums.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAll();

    Order findByOrderId(Long orderId);

    Order save(Order order);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'paid'")
    double getTotalRevenue();

    // Đếm số lượng đơn theo trạng thái
    long countByStatus(OrderStatus status);

    //get Order by status for admin
    @Query("""
            select o from Order o
            where  o.status=:status
            """)
    Page<Order> getOdersByStatus(@Param("status") String status,Pageable pageable);
    
    
}
