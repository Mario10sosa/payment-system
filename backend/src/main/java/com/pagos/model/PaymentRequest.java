package com.pagos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// ★ PATRÓN PROTOTYPE
@Data
public class PaymentRequest implements PaymentPrototype {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;

    // Tarjeta
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    // PayPal
    private String email;

    // Nequi / Daviplata
    private String phone;

    // Crypto
    private String walletAddress;
    private String cryptoCurrency;

    // ★ PATRÓN PROTOTYPE — copia exacta del objeto
    @Override
    public PaymentRequest clone() {
        PaymentRequest copia = new PaymentRequest();
        copia.setAmount(this.amount);
        copia.setPaymentMethod(this.paymentMethod);
        copia.setCardNumber(this.cardNumber);
        copia.setCvv(this.cvv);
        copia.setExpiryDate(this.expiryDate);
        copia.setCardHolderName(this.cardHolderName);
        copia.setEmail(this.email);
        copia.setPhone(this.phone);
        copia.setWalletAddress(this.walletAddress);
        copia.setCryptoCurrency(this.cryptoCurrency);
        return copia;
    }

    // PATRÓN PROTOTYPE — clona y cambia el monto
    public PaymentRequest cloneWithNewAmount(Double newAmount) {
        PaymentRequest copia = this.clone();
        copia.setAmount(newAmount);
        return copia;
    }

    // PATRÓN PROTOTYPE — clona y cambia el método
    public PaymentRequest cloneWithNewMethod(String newMethod) {
        PaymentRequest copia = this.clone();
        copia.setPaymentMethod(newMethod);
        return copia;
    }

    // PATRÓN PROTOTYPE — clona y cambia monto y método
    public PaymentRequest cloneWith(Double newAmount, String newMethod) {
        PaymentRequest copia = this.clone();
        copia.setAmount(newAmount);
        copia.setPaymentMethod(newMethod);
        return copia;
    }
}