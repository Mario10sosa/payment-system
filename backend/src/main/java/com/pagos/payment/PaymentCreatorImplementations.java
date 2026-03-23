package com.pagos.payment;

//1. Creador de Tarjeta
class CreditCardCreator extends PaymentCreator {
    @Override
    public Payment createPayment() {
        return new CreditCardPayment();
    }
}

// 2. Creador de PayPal
class PaypalCreator extends PaymentCreator {
    @Override
    public Payment createPayment() {
        return new PaypalPayment();
    }
}

// 3. Creador de Nequi
class NequiCreator extends PaymentCreator {
    @Override
    public Payment createPayment() {
        return new NequiPayment();
    }
}

// 4. Creador de Daviplata
class DaviplataCreator extends PaymentCreator {
    @Override
    public Payment createPayment() {
        return new DaviplataPayment();
    }
}

// 5. Creador de Crypto
class CryptoCreator extends PaymentCreator {
    @Override
    public Payment createPayment() {
        return new CryptoPayment();
    }
}