package com.pagos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;

/**
 * Integra el Singleton DatabaseConnection
 * con el contexto de Spring Boot.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Expone el Singleton como Bean de Spring.
     * Spring garantiza que este Bean también sea único.
     */
    @Bean
    public DatabaseConnection databaseConnection() {
        return DatabaseConnection.getInstance();
    }

    /**
     * Cierra la conexión cuando Spring
     * apaga la aplicación.
     */
    @PreDestroy
    public void onShutdown() {
        System.out.println("cerrando aplicación...");
        DatabaseConnection.getInstance().closeConnection();
    }
}