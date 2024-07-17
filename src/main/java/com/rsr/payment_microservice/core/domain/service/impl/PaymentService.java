package com.rsr.payment_microservice.core.domain.service.impl;

import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.core.domain.service.interfaces.IPaymentRepository;
import com.rsr.payment_microservice.core.domain.service.interfaces.IPaymentService;
import com.rsr.payment_microservice.port.utils.exceptions.NoSuchPaymentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;

    @Override
    public UUID createPayment(RSRPayment payment) {
        RSRPayment createdPayment = paymentRepository.save(payment);
        return createdPayment.getPaymentId();
    }

    @Override
    public RSRPayment getPaymentByIdAndUserId(UUID paymentId, UUID userId) throws NoSuchPaymentException {
        RSRPayment payment = paymentRepository.findById(paymentId).orElseThrow(NoSuchPaymentException::new);
        if (!Objects.equals(payment.getUserId().toString(), userId.toString())) {
            throw new NoSuchPaymentException();
        }
        return payment;
    }
}
