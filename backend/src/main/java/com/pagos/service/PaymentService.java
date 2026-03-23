package com.pagos.service;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import com.pagos.payment.*;
import com.pagos.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.pagos.user.User;

@Service
public class PaymentService {

    private final AbstractFactoryProvider factoryProvider;
    private final PaymentRepository repository;

    public PaymentService(AbstractFactoryProvider factoryProvider,
            PaymentRepository repository) {
        this.factoryProvider = factoryProvider;
        this.repository = repository;
    }

    // ★ Obtiene el userId del usuario logueado
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return user.getId();
        }
        return null;
    }

    public PaymentResponse process(PaymentRequest request) {
        PaymentResponse response;
        Long userId = getCurrentUserId(); // ★ obtiene el usuario logueado

        try {
            PaymentAbstractFactory factory = factoryProvider.getFactory(request.getPaymentMethod());

            PaymentValidator validator = factory.createValidator();
            validator.validate(request);

            PaymentCreator creator = factory.createCreator();
            response = creator.processPayment(request);

            // ★ Agrega el userId a la respuesta
            if (userId != null) {
                response = new PaymentResponse.Builder()
                        .transactionId(response.getTransactionId())
                        .amount(response.getAmount())
                        .paymentMethod(response.getPaymentMethod())
                        .status(response.getStatus())
                        .receiptId(response.getReceiptId())
                        .timestamp(response.getTimestamp())
                        .errorMessage(response.getErrorMessage())
                        .userId(userId)
                        .build();
            }

        } catch (IllegalArgumentException e) {
            response = PaymentResponse.failure(
                    request.getPaymentMethod(), e.getMessage(), userId);
        } catch (Exception e) {
            response = PaymentResponse.failure(
                    request.getPaymentMethod(), "Error interno: " + e.getMessage(), userId);
        }

        return repository.save(response);
    }

    // ★ PROTOTYPE — clona y cambia el monto
    public PaymentResponse cloneAndProcess(PaymentRequest original, Double newAmount) {
        PaymentRequest clon = original.cloneWithNewAmount(newAmount);
        return process(clon);
    }

    // ★ PROTOTYPE — clona y cambia el método
    public PaymentResponse cloneWithNewMethod(PaymentRequest original, String newMethod) {
        PaymentRequest clon = original.cloneWithNewMethod(newMethod);
        return process(clon);
    }
}