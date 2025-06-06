package com.elearning.haui.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/PurchaseHistory")
public class PurchaseHistoryAPI {
    //get all PurchaseHistory
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        return null;
    }
    //get  PurchaseHistory by id
}
