package com.elearning.haui.controller.Admin;

import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.entity.OrderDetail;
import com.elearning.haui.enums.OrderStatus;
import com.elearning.haui.service.OrderService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get
    @GetMapping("/admin/order")
    public String getOrder(Model model,
            @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        Page<Order> orderPage = orderService.findPaginated(PageRequest.of(page - 1, pageSize));
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        return "/admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "/admin/order/detail";
    }

    // Create
    @GetMapping("/admin/order/create")
    public String getCreateOrder(Model model) {
        model.addAttribute("newOrder", new Order());
        return "/admin/order/create";
    }

    @PostMapping("/admin/order/create")
    public String postCreateOrder(Model model, @ModelAttribute("newOrder") Order order) {
        this.orderService.handleSaveOrder(order);
        return "redirect:/admin/order";
    }

    // Delete
    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteProduct(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("deleteOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteProduct(@ModelAttribute("deleteOrder") Order order) {
        this.orderService.deleteOrderById(order.getOrderId());
        return "redirect:/admin/order";
    }

    // Cập nhật trạng thái đơn hàng
    @GetMapping("/admin/order/updateStatus/{orderId}")
    public String updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") String status) {

        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setStatus(orderStatus);
            orderService.updateOrder(order);
        }
        return "redirect:/admin/order";
    }

}
