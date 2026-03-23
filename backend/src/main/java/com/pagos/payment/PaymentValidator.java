package com.pagos.payment;

import com.pagos.model.PaymentRequest;

// PATRÓN ABSTRACT FACTORY — interface del validador
public interface PaymentValidator {

    void validate(PaymentRequest request);

    String getValidatorName();
}