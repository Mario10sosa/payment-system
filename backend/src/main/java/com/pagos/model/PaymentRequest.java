package com.pagos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod; // CARD, PAYPAL, NEQUI, DAVIPLATA, CRYPTO

    // ── Tarjeta ──────────────────
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    // ── PayPal ───────────────────
    private String email;

    // ── Nequi / Daviplata ────────
    private String phone;

    // ── Crypto ───────────────────
    private String walletAddress;
    private String cryptoCurrency; // BTC, ETH, USDT
}
