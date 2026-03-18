package com.pagos.payment;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Payment {

    // Template Method
    public final PaymentResponse processPayment(PaymentRequest request) {
        validate(request);
        PaymentResponse response = pay(request);
        response = generateReceipt(response);
        return response;
    }

    protected abstract PaymentResponse pay(PaymentRequest request);

    public abstract String getPaymentMethodName();

    protected void validate(PaymentRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank())
            throw new IllegalArgumentException("El método de pago es obligatorio");
    }

    // ★ Ahora usa el Builder para construir el recibo
    protected PaymentResponse generateReceipt(PaymentResponse response) {
        return new PaymentResponse.Builder()
                .transactionId(response.getTransactionId())
                .amount(response.getAmount())
                .paymentMethod(response.getPaymentMethod())
                .status(response.getStatus())
                .receiptId("REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .timestamp(LocalDateTime.now())
                .errorMessage(response.getErrorMessage())
                .build();
    }
}