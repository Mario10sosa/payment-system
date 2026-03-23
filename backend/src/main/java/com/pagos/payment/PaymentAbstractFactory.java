package com.pagos.payment;

// PATRÓN ABSTRACT FACTORY
public interface PaymentAbstractFactory {

    Payment createPayment();

    PaymentValidator createValidator();

    PaymentCreator createCreator();

    default String getFactoryName() {
        return this.getClass().getSimpleName();
    }
}