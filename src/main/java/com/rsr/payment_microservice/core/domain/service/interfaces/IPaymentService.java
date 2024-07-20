package com.rsr.payment_microservice.core.domain.service.interfaces;

import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.port.utils.exceptions.NoSuchPaymentException;

import java.util.UUID;

public interface IPaymentService {

    void createPayment(RSRPayment payment);

    RSRPayment getPaymentByOrderIdAndUserId(UUID orderId, UUID userId) throws NoSuchPaymentException;
}
