package com.pagos.model;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

// ★ PATRÓN BUILDER
@Getter
@Entity
@Table(name = "payments")
public class PaymentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private Double amount;
    private String paymentMethod;
    private String status;
    private String receiptId;
    private LocalDateTime timestamp;
    private String errorMessage;

    protected PaymentResponse() {
    }

    // PATRÓN BUILDER
    public static class Builder {

        private String transactionId;
        private Double amount;
        private String paymentMethod;
        private String status;
        private String receiptId;
        private LocalDateTime timestamp;
        private String errorMessage;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder receiptId(String receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        // PATRÓN BUILDER — ensambla el objeto final
        public PaymentResponse build() {
            if (status == null || status.isBlank())
                throw new IllegalStateException("El status es obligatorio");
            if (paymentMethod == null || paymentMethod.isBlank())
                throw new IllegalStateException("El método de pago es obligatorio");

            PaymentResponse response = new PaymentResponse();
            response.transactionId = this.transactionId;
            response.amount = this.amount;
            response.paymentMethod = this.paymentMethod;
            response.status = this.status;
            response.receiptId = this.receiptId;
            response.timestamp = this.timestamp;
            response.errorMessage = this.errorMessage;
            return response;
        }
    }

    // PATRÓN BUILDER — pago exitoso
    public static PaymentResponse success(String transactionId,
            Double amount,
            String method) {
        return new Builder()
                .transactionId(transactionId)
                .amount(amount)
                .paymentMethod(method)
                .status("SUCCESS")
                .build();
    }

    // PATRÓN BUILDER — pago fallido
    public static PaymentResponse failure(String method, String error) {
        return new Builder()
                .paymentMethod(method)
                .status("FAILED")
                .errorMessage(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}