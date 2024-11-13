package com.elearning.haui.controller.Admin;

import com.elearning.haui.entity.Course;
import com.elearning.haui.entity.Order;
import com.elearning.haui.service.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getOrderById(Model model, @PathVariable Long id) {
        Order order = this.orderService.getOrderById(id);
        model.addAttribute("course", order);
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

}
