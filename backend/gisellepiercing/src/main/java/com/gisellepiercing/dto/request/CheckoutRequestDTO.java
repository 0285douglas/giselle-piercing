package com.gisellepiercing.dto.request;

import com.gisellepiercing.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {
    private PaymentMethod paymentMethod;
}