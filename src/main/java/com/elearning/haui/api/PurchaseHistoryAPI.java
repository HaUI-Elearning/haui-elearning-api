package com.elearning.haui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.service.paymentsService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/PurchaseHistory")
public class PurchaseHistoryAPI {
    @Autowired 
    paymentsService paymentsService;

    //get all by User
    @Operation(summary = "Lấy all lịch sử mua bởi user ")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(Authentication authentication){
        return ResponseEntity.ok(paymentsService.getAllHistoryPurcharses(authentication.getName()));
    }

    //get all PurchaseHistory by Status
    @Operation(summary = "Lấy all lịch sử mua bởi user theo status {success/failed}")
    @GetMapping("/getAll/Status")
    public ResponseEntity<?> getAllbyStatus(Authentication authentication,@RequestParam String status){
        return ResponseEntity.ok(paymentsService.getAllHistoryPurcharsesByStatus(authentication.getName(),status));
    }
    //get  PurchaseHistory by id
    @Operation(summary = "Lấy lịch sử mua bởi user theo id payment")
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getByPaymentId(Authentication authentication,@PathVariable("paymentId") Long paymentId)
    {
        return ResponseEntity.ok(paymentsService.getById(authentication.getName(), paymentId));
    }
}
