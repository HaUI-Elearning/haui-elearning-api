package com.elearning.haui.service;

import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.repository.OrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> findPaginated(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }

    public List<Order> getAllOrderById() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return this.orderRepository.findByOrderId(id);
    }

    public void handleSaveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public void updateOrder(Order order) {
        this.orderRepository.save(order);
    }
}
