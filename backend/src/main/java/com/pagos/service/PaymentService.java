package com.pagos.service;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import com.pagos.payment.Payment;
import com.pagos.payment.PaymentFactory;
import com.pagos.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentFactory factory;
    private final PaymentRepository repository;

    public PaymentService(PaymentFactory factory, PaymentRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    public PaymentResponse process(PaymentRequest request) {
        PaymentResponse response;

        try {
            // 1. Factory decide qué Payment usar
            Payment payment = factory.create(request.getPaymentMethod());

            // 2. Template Method: validate → pay → generateReceipt
            response = payment.processPayment(request);

        } catch (IllegalArgumentException e) {
            response = PaymentResponse.failure(request.getPaymentMethod(), e.getMessage());
        } catch (Exception e) {
            response = PaymentResponse.failure(request.getPaymentMethod(), "Error interno: " + e.getMessage());
        }

        // 3. Guardar en MySQL
        return repository.save(response);
    }
}