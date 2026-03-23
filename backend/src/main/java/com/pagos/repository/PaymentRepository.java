package com.pagos.repository;

import com.pagos.model.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentResponse, Long> {

    // Consultas generadas por nombre de método
    // Busca pagos por método de pago
    List<PaymentResponse> findByPaymentMethodOrderByTimestampDesc(
            String paymentMethod);

    // Busca pagos por estado
    List<PaymentResponse> findByStatusOrderByTimestampDesc(
            String status);

    // Busca pagos exitosos por método de pago.

    List<PaymentResponse> findByPaymentMethodAndStatus(
            String paymentMethod,
            String status);

    // Cuenta los pagos por estado.
    Long countByStatus(String status);

    // Cuenta los pagos por método de pago.
    Long countByPaymentMethod(String paymentMethod);

    // Consultas JPQL personalizadas

    // Obtiene los últimos 10 pagos realizados.
    @Query("SELECT p FROM PaymentResponse p " +
            "ORDER BY p.timestamp DESC " +
            "LIMIT 10")
    List<PaymentResponse> findLast10Payments();

    /**
     * Calcula el total de dinero procesado
     * solo de pagos exitosos.
     */
    @Query("SELECT SUM(p.amount) FROM PaymentResponse p " +
            "WHERE p.status = 'SUCCESS'")
    Double getTotalAmountProcessed();
}