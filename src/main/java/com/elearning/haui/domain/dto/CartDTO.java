package com.elearning.haui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private long cartId;
    private int quantity;
    private Double totalPrice;
    private List<CartDetailDTO> cartDetails;
}
