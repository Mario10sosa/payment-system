package com.pagos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN SINGLETON APLICADO EN ESTA CLASE
 */
public class DatabaseConnection {

    // La única instancia — static para que sea de la clase
    private static DatabaseConnection instance;

    // Conexión real a MySQL
    private Connection connection;

    // Datos de conexión — deben coincidir con application.properties
    // private static final String URL =
    // "jdbc:mysql://localhost:3306/pagos_db?useSSL=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://localhost:3306/pagos_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    // ══════════════════════════════════════════
    // CONSTRUCTOR PRIVADO
    // ══════════════════════════════════════════
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("conexión a MySQL establecida");
            System.out.println("instancia → " + this.hashCode());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar MySQL: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════
    // MÉTODO getInstance() — CORAZÓN del patrón
    // Único punto de acceso global a la instancia.
    // ══════════════════════════════════════════
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            // Solo se ejecuta UNA vez en toda la aplicación
            System.out.println("creando única instancia...");
            instance = new DatabaseConnection();
        } else {
            System.out.println("reutilizando instancia → " + instance.hashCode());
        }
        return instance;
    }

    // ══════════════════════════════════════════
    // Métodos de gestión de la conexión
    // ══════════════════════════════════════════

    /**
     * Retorna la conexión activa a MySQL.
     * Si se cerró, la reconecta automáticamente.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("reconectando a MySQL...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Verifica si la conexión está activa.
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Retorna información de la instancia actual.
     */
    public String getInstanceInfo() {
        return "DatabaseConnection@" + Integer.toHexString(this.hashCode());
    }

    /**
     * Cierra la conexión cuando la aplicación termina.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                instance = null;
                System.out.println("conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}