package com.gisellepiercing.dto.request;

import com.gisellepiercing.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {

    @NotBlank(message = "O método de pagamento é obrigatório")
    private String paymentMethod;

    private String token;
    private Integer installments;
    private String paymentMethodId;
}