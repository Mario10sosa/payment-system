package com.pagos.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    public Payment create(String type) {
        if (type == null)
            throw new IllegalArgumentException("Tipo de pago nulo");

        return switch (type.toUpperCase().trim()) {
            case "CARD" -> new CreditCardPayment();
            case "PAYPAL" -> new PaypalPayment();
            case "NEQUI" -> new NequiPayment();
            case "DAVIPLATA" -> new DaviplataPayment();
            case "CRYPTO" -> new CryptoPayment();
            default -> throw new IllegalArgumentException("Método no soportado: " + type);
        };
    }
}
