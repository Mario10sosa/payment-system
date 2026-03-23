package com.pagos.controller;

import com.pagos.model.PaymentRequest;
import com.pagos.model.PaymentResponse;
import com.pagos.repository.PaymentRepository;
import com.pagos.service.PaymentService;
import com.pagos.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService service;
    private final PaymentRepository repository;

    public PaymentController(PaymentService service,
            PaymentRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // POST /api/payments/pay
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = service.process(request);
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ★ PROTOTYPE — clona y cambia el monto
    @PostMapping("/clone")
    public ResponseEntity<PaymentResponse> cloneAndPay(
            @Valid @RequestBody PaymentRequest original,
            @RequestParam Double newAmount) {
        PaymentResponse response = service.cloneAndProcess(original, newAmount);
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ★ PROTOTYPE — clona y cambia el método
    @PostMapping("/clone-method")
    public ResponseEntity<PaymentResponse> cloneWithMethod(
            @Valid @RequestBody PaymentRequest original,
            @RequestParam String newMethod) {
        PaymentResponse response = service.cloneWithNewMethod(original, newMethod);
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ★ GET /api/payments/list — pagos del usuario logueado
    @GetMapping("/list")
    public ResponseEntity<?> getAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<PaymentResponse> pagos = repository
                .findByUserIdOrderByTimestampDesc(user.getId());
        return ResponseEntity.ok(pagos);
    }

    // GET /api/payments/my-payments — historial del usuario logueado
    @GetMapping("/my-payments")
    public ResponseEntity<?> getMyPayments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<PaymentResponse> pagos = repository
                .findByUserIdOrderByTimestampDesc(user.getId());
        return ResponseEntity.ok(pagos);
    }

    // GET /api/payments/methods
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