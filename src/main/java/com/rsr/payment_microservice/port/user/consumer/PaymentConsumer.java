package com.rsr.payment_microservice.port.user.consumer;


import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.core.domain.service.interfaces.IPaymentService;
import com.rsr.payment_microservice.port.user.dto.PaymentRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentConsumer {

    @Autowired
    private IPaymentService paymentService;

    @RabbitListener(queues = {"${rabbitmq.payment.queue.name}"})
    public void consume(PaymentRequestDto paymentRequestDto) {
        RSRPayment payment = new RSRPayment();
        payment.setPaymentMethod(paymentRequestDto.getPaymentMethod());
        payment.setOrderId(paymentRequestDto.getOrderId());
        payment.setUserId(paymentRequestDto.getUserId());
        payment.setAmountInEuro(paymentRequestDto.getAmountInEuro());

        paymentService.createPayment(payment);
    }

}
