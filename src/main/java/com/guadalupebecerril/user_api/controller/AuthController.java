package com.guadalupebecerril.user_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guadalupebecerril.user_api.dto.LoginRequest;
import com.guadalupebecerril.user_api.service.UserService;

/**
 * Controlador encargado de gestionar los procesos de autenticación.
 * Proporciona los endpoints necesarios para validar la identidad de los
 * usuarios.
 */
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para el inicio de sesión de usuarios.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var user = userService.login(request.getTaxId(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas.");
        }
    }
}