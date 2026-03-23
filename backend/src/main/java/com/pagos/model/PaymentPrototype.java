package com.pagos.model;

/**
 * PATRÓN PROTOTYPE
 */
public interface PaymentPrototype {

    PaymentRequest clone();
}