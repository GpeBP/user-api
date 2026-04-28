package com.guadalupebecerril.user_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guadalupebecerril.user_api.service.UserService;

/**
 * Controlador encargado de gestionar los procesos de autenticación.
 * Proporciona los endpoints necesarios para validar la identidad de los
 * usuarios.
 */
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint para el inicio de sesión de usuarios.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String tax_id, @RequestParam String password) {
        try {
            var user = userService.login(tax_id, password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Identificación de errores de credenciales (Usuario no encontrado o Contraseña
            // incorrecta)
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}