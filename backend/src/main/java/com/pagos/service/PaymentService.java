package com.pagos.service;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import com.pagos.payment.*;
import com.pagos.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final AbstractFactoryProvider factoryProvider;
    private final PaymentRepository repository;

    public PaymentService(AbstractFactoryProvider factoryProvider,
            PaymentRepository repository) {
        this.factoryProvider = factoryProvider;
        this.repository = repository;
    }

    // ══════════════════════════════════════════
    // Procesar pago normal
    // ══════════════════════════════════════════
    public PaymentResponse process(PaymentRequest request) {
        PaymentResponse response;

        try {
            // ABSTRACT FACTORY
            // Obtiene la familia completa según el tipo
            PaymentAbstractFactory factory = factoryProvider.getFactory(request.getPaymentMethod());

            System.out.println(" usando → "
                    + factory.getFactoryName());

            // Crea el validador y valida datos específicos
            PaymentValidator validator = factory.createValidator();
            System.out.println(" Validador: → " + validator.getValidatorName());
            validator.validate(request);

            // FACTORY METHOD
            // Crea el Creator que instancia el Payment
            PaymentCreator creator = factory.createCreator();
            System.out.println("  usando → "
                    + creator.getCreatorName());

            // TEMPLATE METHOD + ABSTRACT METHOD
            response = creator.processPayment(request);

        } catch (IllegalArgumentException e) {
            // BUILDER — construye respuesta de error
            response = PaymentResponse.failure(
                    request.getPaymentMethod(),
                    e.getMessage());
        } catch (Exception e) {
            response = PaymentResponse.failure(
                    request.getPaymentMethod(),
                    "Error interno: " + e.getMessage());
        }

        // Guarda en MySQL via JPA
        return repository.save(response);
    }

    // PATRÓN PROTOTYPE
    // Clona un pago y cambia el monto

    /**
     * Clona el PaymentRequest original y
     * procesa un nuevo pago con diferente monto.
     */
    public PaymentResponse cloneAndProcess(PaymentRequest original,
            Double newAmount) {
        System.out.println("clonando request original...");
        System.out.println("   Original → " + original.getPaymentMethod()
                + " $" + original.getAmount());

        // PROTOTYPE — clona y cambia el monto
        PaymentRequest clon = original.cloneWithNewAmount(newAmount);

        System.out.println("    Clon     → " + clon.getPaymentMethod()
                + " $" + clon.getAmount());

        // Procesa el pago clonado
        return process(clon);
    }

    /**
     * Clona el PaymentRequest original y
     * procesa un nuevo pago con diferente método.
     */
    public PaymentResponse cloneWithNewMethod(PaymentRequest original,
            String newMethod) {
        System.out.println("clonando con nuevo método...");

        // PROTOTYPE — clona y cambia el método
        PaymentRequest clon = original.cloneWithNewMethod(newMethod);

        return process(clon);
    }

}