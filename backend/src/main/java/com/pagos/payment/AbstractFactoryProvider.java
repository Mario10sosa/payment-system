package com.pagos.payment;

import org.springframework.stereotype.Component;

// PATRÓN ABSTRACT FACTORY — selector de fábrica
@Component
public class AbstractFactoryProvider {

    public PaymentAbstractFactory getFactory(String type) {
        if (type == null)
            throw new IllegalArgumentException("Tipo de pago nulo");

        return switch (type.toUpperCase().trim()) {
            case "CARD" -> new CardAbstractFactory();
            case "PAYPAL" -> new PaypalAbstractFactory();
            case "NEQUI" -> new NequiAbstractFactory();
            case "DAVIPLATA" -> new DaviplataAbstractFactory();
            case "CRYPTO" -> new CryptoAbstractFactory();
            default -> throw new IllegalArgumentException(
                    "Método no soportado: " + type);
        };
    }
}