package com.rsr.payment_microservice.core.domain.service.interfaces;

import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.port.utils.exceptions.NoSuchPaymentException;

import java.util.UUID;

public interface IPaymentService {

    UUID createPayment(RSRPayment payment);

    RSRPayment getPaymentByIdAndUserId(UUID paymentId, UUID userId) throws NoSuchPaymentException;
}
