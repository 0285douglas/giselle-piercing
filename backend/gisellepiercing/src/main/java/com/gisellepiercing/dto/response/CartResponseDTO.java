package com.gisellepiercing.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CartResponseDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private BigDecimal total;
}