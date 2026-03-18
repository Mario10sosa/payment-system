package com.pagos.controller;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import com.pagos.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // POST /api/payments/pay — procesa el pago
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = service.process(request);

        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET /api/payments/methods — lista de métodos disponibles
    @GetMapping("/methods")
    public ResponseEntity<List<Map<String, String>>> getMethods() {
        return ResponseEntity.ok(List.of(
                Map.of("id", "CARD", "label", "Tarjeta", "icon", "💳"),
                Map.of("id", "PAYPAL", "label", "PayPal", "icon", "🅿️"),
                Map.of("id", "NEQUI", "label", "Nequi", "icon", "🟣"),
                Map.of("id", "DAVIPLATA", "label", "Daviplata", "icon", "🔴"),
                Map.of("id", "CRYPTO", "label", "Criptomonedas", "icon", "₿")));
    }
}