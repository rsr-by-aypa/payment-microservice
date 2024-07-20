package com.rsr.payment_microservice.port.user.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.core.domain.service.impl.PaypalService;
import com.rsr.payment_microservice.core.domain.service.interfaces.IPaymentService;
import com.rsr.payment_microservice.port.utils.exceptions.NoSuchPaymentException;
import com.rsr.payment_microservice.port.utils.exceptions.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/payment/paypal")
public class PaypalController {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private PaypalService paypalService;

    @PostMapping("/create/{userId}")
    public String createPayment(@RequestParam("cancelUrl") String cancelUrl,
                                @RequestParam("successUrl") String successUrl,
                                @PathVariable("userId") UUID userId,
                                @RequestParam("orderId") UUID orderId) throws NoSuchPaymentException, PayPalRESTException, PaymentException {

        RSRPayment rsrPayment = paymentService.getPaymentByOrderIdAndUserId(orderId, userId);
        double totalAmount = rsrPayment.getAmountInEuro();

        Payment payment = paypalService.createPayment(10.0, "EUR", "paypal",
                "sale", "Your Order - 12", cancelUrl, successUrl);
        for (Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return links.getHref();
            }
        }
        throw new PaymentException();
    }

    @GetMapping("/execute")
    public String executePayment(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("payerId") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occured");
        }
        return "paymentSuccess";
    }

}
