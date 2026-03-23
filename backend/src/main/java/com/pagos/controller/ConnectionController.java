package com.pagos.controller;

import com.pagos.config.DatabaseConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Endpoint para verificar el estado del Singleton.
 * Útil para demostrar el patrón en la sustentación.
 */
@RestController
@RequestMapping("/api/db")
@CrossOrigin(origins = "http://localhost:5173")
public class ConnectionController {

    private final DatabaseConnection dbConnection;

    public ConnectionController(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * GET /api/db/status
     * Verifica que el Singleton está activo.
     * El mismo hashCode siempre = misma instancia.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
                "pattern", "Singleton",
                "connected", dbConnection.isConnected(),
                "instance", dbConnection.getInstanceInfo(),
                "hashCode", dbConnection.hashCode(),
                "message", dbConnection.isConnected()
                        ? "Conexión activa — instancia única garantizada"
                        : "Sin conexión"));
    }
}