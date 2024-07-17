package com.rsr.payment_microservice.port.utils.exceptions;

public class PaymentException extends Exception {
    public PaymentException() {
        super("Failed to generate Payment");
    }
}
