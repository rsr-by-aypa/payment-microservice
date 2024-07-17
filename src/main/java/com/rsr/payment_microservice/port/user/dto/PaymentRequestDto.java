package com.rsr.payment_microservice.port.user.dto;


import com.rsr.payment_microservice.core.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentRequestDto {
    private UUID orderId;
    private UUID userId;
    private Double amountInEuro;
    private PaymentMethod paymentMethod;
}
