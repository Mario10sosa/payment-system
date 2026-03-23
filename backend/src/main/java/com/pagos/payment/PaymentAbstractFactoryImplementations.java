package com.pagos.payment;

// PATRÓN ABSTRACT FACTORY — fábricas concretas

class CardAbstractFactory implements PaymentAbstractFactory {

    @Override
    public Payment createPayment() {
        return new CreditCardPayment();
    }

    @Override
    public PaymentValidator createValidator() {
        return new CardValidator();
    }

    @Override
    public PaymentCreator createCreator() {
        return new CreditCardCreator();
    }
}

class PaypalAbstractFactory implements PaymentAbstractFactory {

    @Override
    public Payment createPayment() {
        return new PaypalPayment();
    }

    @Override
    public PaymentValidator createValidator() {
        return new PaypalValidator();
    }

    @Override
    public PaymentCreator createCreator() {
        return new PaypalCreator();
    }
}

class NequiAbstractFactory implements PaymentAbstractFactory {

    @Override
    public Payment createPayment() {
        return new NequiPayment();
    }

    @Override
    public PaymentValidator createValidator() {
        return new NequiValidator();
    }

    @Override
    public PaymentCreator createCreator() {
        return new NequiCreator();
    }
}

class DaviplataAbstractFactory implements PaymentAbstractFactory {

    @Override
    public Payment createPayment() {
        return new DaviplataPayment();
    }

    @Override
    public PaymentValidator createValidator() {
        return new DaviplataValidator();
    }

    @Override
    public PaymentCreator createCreator() {
        return new DaviplataCreator();
    }
}

class CryptoAbstractFactory implements PaymentAbstractFactory {

    @Override
    public Payment createPayment() {
        return new CryptoPayment();
    }

    @Override
    public PaymentValidator createValidator() {
        return new CryptoValidator();
    }

    @Override
    public PaymentCreator createCreator() {
        return new CryptoCreator();
    }
}