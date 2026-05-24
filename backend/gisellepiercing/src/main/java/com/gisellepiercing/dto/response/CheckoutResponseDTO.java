package com.gisellepiercing.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckoutResponseDTO {
    private Long orderId;
    private String qrCode;
    private String qrCodeBase64;
    private String boletoUrl;
    private String status;
}