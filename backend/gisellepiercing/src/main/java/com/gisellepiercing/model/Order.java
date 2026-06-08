package com.gisellepiercing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private BigDecimal total;
    private String paymentMethod;
    private String mercadoPagoId;
    private String paymentUrl;
    private OrderStatus status;
}