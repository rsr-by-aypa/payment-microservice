package com.rsr.payment_microservice.port.utils.exceptions;

public class NoSuchPaymentException extends Exception {
    public NoSuchPaymentException() {
        super("There is not Payment with that paymentId and userId");
    }
}
