package com.pagos.payment;

import java.util.UUID;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;

// ─────────────────────────────────────────
// 1. TARJETA
// ─────────────────────────────────────────
class CreditCardPayment extends Payment {

    @Override
    protected PaymentResponse pay(PaymentRequest request) {
        String id = "CARD-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return PaymentResponse.success(id, request.getAmount(), getPaymentMethodName());
    }

    @Override
    protected void validate(PaymentRequest request) {
        super.validate(request);
        if (request.getCardNumber() == null || request.getCardNumber().length() < 16)
            throw new IllegalArgumentException("Número de tarjeta inválido");
        if (request.getCvv() == null || request.getCvv().length() != 3)
            throw new IllegalArgumentException("CVV inválido");
    }

    @Override
    public String getPaymentMethodName() {
        return "Tarjeta de Crédito/Débito";
    }
}

// ─────────────────────────────────────────
// 2. PAYPAL
// ─────────────────────────────────────────
class PaypalPayment extends Payment {

    @Override
    protected PaymentResponse pay(PaymentRequest request) {
        String id = "PP-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return PaymentResponse.success(id, request.getAmount(), getPaymentMethodName());
    }

    @Override
    protected void validate(PaymentRequest request) {
        super.validate(request);
        if (request.getEmail() == null || !request.getEmail().contains("@"))
            throw new IllegalArgumentException("Email de PayPal inválido");
    }

    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
}

// ─────────────────────────────────────────
// 3. NEQUI
// ─────────────────────────────────────────
class NequiPayment extends Payment {

    @Override
    protected PaymentResponse pay(PaymentRequest request) {
        String id = "NEQ-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return PaymentResponse.success(id, request.getAmount(), getPaymentMethodName());
    }

    @Override
    protected void validate(PaymentRequest request) {
        super.validate(request);
        if (request.getPhone() == null || request.getPhone().length() != 10)
            throw new IllegalArgumentException("Teléfono Nequi inválido (debe tener 10 dígitos)");
    }

    @Override
    public String getPaymentMethodName() {
        return "Nequi";
    }
}

// ─────────────────────────────────────────
// 4. DAVIPLATA
// ─────────────────────────────────────────
class DaviplataPayment extends Payment {

    @Override
    protected PaymentResponse pay(PaymentRequest request) {
        String id = "DAVI-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return PaymentResponse.success(id, request.getAmount(), getPaymentMethodName());
    }

    @Override
    protected void validate(PaymentRequest request) {
        super.validate(request);
        if (request.getPhone() == null || request.getPhone().length() != 10)
            throw new IllegalArgumentException("Teléfono Daviplata inválido (debe tener 10 dígitos)");
    }

    @Override
    public String getPaymentMethodName() {
        return "Daviplata";
    }
}

// ─────────────────────────────────────────
// 5. CRIPTOMONEDAS
// ─────────────────────────────────────────
class CryptoPayment extends Payment {

    @Override
    protected PaymentResponse pay(PaymentRequest request) {
        String id = "CRYPTO-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        return PaymentResponse.success(id, request.getAmount(), getPaymentMethodName());
    }

    @Override
    protected void validate(PaymentRequest request) {
        super.validate(request);
        if (request.getWalletAddress() == null || request.getWalletAddress().length() < 26)
            throw new IllegalArgumentException("Dirección de wallet inválida");
    }

    @Override
    public String getPaymentMethodName() {
        return "Criptomonedas";
    }
}