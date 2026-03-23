package com.pagos.payment;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;

//  PATRÓN FACTORY METHOD
public abstract class PaymentCreator {

    // PATRÓN FACTORY METHOD — cada subclase decide qué Payment crear
    public abstract Payment createPayment();

    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = createPayment();
        return payment.processPayment(request);
    }

    public String getCreatorName() {
        return this.getClass().getSimpleName();
    }
}