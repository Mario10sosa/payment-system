package com.pagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pagos.model.PaymentResponse;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentResponse, Long> {

    List<PaymentResponse> findByPaymentMethodOrderByTimestampDesc(String paymentMethod);

    List<PaymentResponse> findByStatusOrderByTimestampDesc(String status);
}