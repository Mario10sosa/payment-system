package com.pagos.payment;

import com.pagos.model.PaymentRequest;

//  PATRÓN ABSTRACT FACTORY — validadores concretos

class CardValidator implements PaymentValidator {

    @Override
    public void validate(PaymentRequest request) {
        if (request.getCardNumber() == null
                || request.getCardNumber().length() < 16)
            throw new IllegalArgumentException(
                    "Número de tarjeta inválido (debe tener 16 dígitos)");
        if (request.getCvv() == null
                || request.getCvv().length() != 3)
            throw new IllegalArgumentException(
                    "CVV inválido (debe tener 3 dígitos)");
        if (request.getExpiryDate() == null
                || request.getExpiryDate().isBlank())
            throw new IllegalArgumentException(
                    "Fecha de vencimiento obligatoria");
        if (request.getCardHolderName() == null
                || request.getCardHolderName().isBlank())
            throw new IllegalArgumentException(
                    "Nombre del titular obligatorio");
    }

    @Override
    public String getValidatorName() {
        return "CardValidator";
    }
}

class PaypalValidator implements PaymentValidator {

    @Override
    public void validate(PaymentRequest request) {
        if (request.getEmail() == null
                || request.getEmail().isBlank())
            throw new IllegalArgumentException(
                    "Email de PayPal obligatorio");
        if (!request.getEmail().contains("@"))
            throw new IllegalArgumentException(
                    "Email de PayPal inválido");
    }

    @Override
    public String getValidatorName() {
        return "PaypalValidator";
    }
}

class NequiValidator implements PaymentValidator {

    @Override
    public void validate(PaymentRequest request) {
        if (request.getPhone() == null
                || request.getPhone().isBlank())
            throw new IllegalArgumentException(
                    "Teléfono Nequi obligatorio");
        if (request.getPhone().length() != 10)
            throw new IllegalArgumentException(
                    "Teléfono Nequi inválido (debe tener 10 dígitos)");
        if (!request.getPhone().matches("\\d+"))
            throw new IllegalArgumentException(
                    "Teléfono Nequi solo debe contener números");
    }

    @Override
    public String getValidatorName() {
        return "NequiValidator";
    }
}

class DaviplataValidator implements PaymentValidator {

    @Override
    public void validate(PaymentRequest request) {
        if (request.getPhone() == null
                || request.getPhone().isBlank())
            throw new IllegalArgumentException(
                    "Teléfono Daviplata obligatorio");
        if (request.getPhone().length() != 10)
            throw new IllegalArgumentException(
                    "Teléfono Daviplata inválido (debe tener 10 dígitos)");
        if (!request.getPhone().matches("\\d+"))
            throw new IllegalArgumentException(
                    "Teléfono Daviplata solo debe contener números");
    }

    @Override
    public String getValidatorName() {
        return "DaviplataValidator";
    }
}

class CryptoValidator implements PaymentValidator {

    @Override
    public void validate(PaymentRequest request) {
        if (request.getWalletAddress() == null
                || request.getWalletAddress().isBlank())
            throw new IllegalArgumentException(
                    "Dirección de wallet obligatoria");
        if (request.getWalletAddress().length() < 26)
            throw new IllegalArgumentException(
                    "Dirección de wallet inválida (mínimo 26 caracteres)");
        if (request.getCryptoCurrency() == null
                || request.getCryptoCurrency().isBlank())
            throw new IllegalArgumentException(
                    "Criptomoneda obligatoria (BTC, ETH, USDT)");
    }

    @Override
    public String getValidatorName() {
        return "CryptoValidator";
    }
}