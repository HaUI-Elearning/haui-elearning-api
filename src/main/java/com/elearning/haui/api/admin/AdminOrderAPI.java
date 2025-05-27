package com.elearning.haui.api.admin;

import com.elearning.haui.domain.dto.OrderDTO;
import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.dto.UpdateOrderStatusDTO;
import com.elearning.haui.domain.entity.Order;
import com.elearning.haui.domain.response.RestResponse;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.service.OrderService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/Admin/order")
public class AdminOrderAPI {

    private final OrderService orderService;

    public AdminOrderAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    //Padding order
    @GetMapping("")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional
    ){
        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);
        Pageable pageable = PageRequest.of(current -1, pageSize);
        ResultPaginationDTO result = orderService.fetchAllCourses(pageable);

        return ResponseEntity.ok(result);
    }

    //Get order detial by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @PathVariable long id
    ){
        OrderDTO rs = orderService.getOrderDTOById(id);
        return ResponseEntity.ok(rs);
    }

    //Delte order by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(
            @PathVariable long id
    ){
        orderService.deleteOrderById(id);

        RestResponse restResponse = new RestResponse();
        restResponse.setMessage("Deleted order successfully");
        restResponse.setData(null);
        return ResponseEntity.ok(restResponse);
    }

    //Update status
    @PostMapping("")
    public ResponseEntity<?> updateOrder(
            @RequestBody UpdateOrderStatusDTO requestDTO) {
        orderService.updateOrderStatus(requestDTO.getId(), requestDTO.getStatus());

        RestResponse<?> restResponse = new RestResponse<>();
        restResponse.setMessage("Order status updated successfully");
        restResponse.setData(null);
        return ResponseEntity.ok(restResponse);
    }
}
