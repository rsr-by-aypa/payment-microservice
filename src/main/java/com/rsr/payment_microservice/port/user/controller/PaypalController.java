package com.rsr.payment_microservice.port.user.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.rsr.payment_microservice.core.domain.model.RSRPayment;
import com.rsr.payment_microservice.core.domain.service.impl.PaypalService;
import com.rsr.payment_microservice.core.domain.service.interfaces.IPaymentService;
import com.rsr.payment_microservice.port.utils.exceptions.NoSuchPaymentException;
import com.rsr.payment_microservice.port.utils.exceptions.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment/paypal")
public class PaypalController {

    @Autowired
    private IPaymentService paymentService;

    private final PaypalService paypalService;

    @PostMapping("/create")
    public RedirectView createPayment(@RequestParam("cancelUrl") String cancelUrl,
                                      @RequestParam("successUrl") String successUrl,
                                      @RequestParam("paymentId") UUID paymentId,
                                      @RequestParam("userId") UUID userId) throws NoSuchPaymentException, PayPalRESTException, PaymentException {

        RSRPayment rsrPayment = paymentService.getPaymentByIdAndUserId(paymentId, userId);
        double totalAmount = rsrPayment.getAmountInEuro();
        UUID orderId = rsrPayment.getOrderId();

        Payment payment = paypalService.createPayment(totalAmount, "EUR", "paypal",
                "sale", "Your Order - " + orderId, cancelUrl, successUrl);
        for (Links links: payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return new RedirectView(links.getHref());
            }
        }
        throw new PaymentException();
    }

    @GetMapping("/execute")
    public String executePayment(@RequestParam("cancelUrl") String cancelUrl,
                                 @RequestParam("successUrl") String successUrl,
                                 @RequestParam("paymentId") String paymentId,
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
