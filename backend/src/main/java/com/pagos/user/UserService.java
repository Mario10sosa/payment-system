package com.pagos.user;

import com.pagos.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ★ Registrar usuario nuevo
    public Map<String, Object> register(String nombre,
            String email,
            String password) {
        if (repository.existsByEmail(email))
            throw new IllegalArgumentException("El email ya está registrado");

        // Encripta la contraseña con BCrypt
        String passwordEncriptada = passwordEncoder.encode(password);
        User user = new User(nombre, email, passwordEncriptada);
        User saved = repository.save(user);

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getId());

        return Map.of(
                "message", "Usuario registrado exitosamente",
                "token", token,
                "user", Map.of(
                        "id", saved.getId(),
                        "nombre", saved.getNombre(),
                        "email", saved.getEmail()));
    }

    // ★ Login de usuario
    public Map<String, Object> login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email no registrado"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("Contraseña incorrecta");

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return Map.of(
                "message", "Login exitoso",
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "nombre", user.getNombre(),
                        "email", user.getEmail()));
    }

    // ★ Obtener perfil del usuario
    public Map<String, Object> getProfile(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return Map.of(
                "id", user.getId(),
                "nombre", user.getNombre(),
                "email", user.getEmail());
    }
}