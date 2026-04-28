package com.guadalupebecerril.user_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.guadalupebecerril.user_api.model.User;
import com.guadalupebecerril.user_api.service.UserService;

/**
 * Pruebas unitarias para el servicio de usuarios.
 * Valida la lógica, reglas de validación y procesos de seguridad.
 */
public class UserServiceTest {

    private UserService userService;

    /**
     * Configuración previa a cada ejecución de test.
     * Garantiza que cada prueba trabaje con una instancia limpia del servicio.
     */
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    /**
     * Escenario: Registro exitoso de un usuario.
     * Verifica que el sistema asigne IDs y mantenga la integridad de los datos
     * cuando la entrada cumple con todas las reglas.
     */
    @Test
    void testSaveUser_Success() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setTax_id("VAMI920425ABC");
        user.setPhone("5512345678");
        user.setPassword("password123");

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
    }

    /**
     * Escenario: Intento de registro con un RFC mal formado.
     * Verifica que el sistema bloquee la persistencia y lance una excepción
     */
    @Test
    void testSaveUser_InvalidRFC_ShouldThrowException() {
        User user = new User();
        user.setTax_id("RFC-INVALIDO");

        // Verificamos que lance la excepción 400
        assertThrows(ResponseStatusException.class, () -> {
            userService.saveUser(user);
        });
    }

    /**
     * Escenario: Intento de inicio de sesión con credenciales erróneas.
     * Valida que el proceso de autenticación rechace accesos con contraseñas
     * incorrectas.
     */
    @Test
    void testLogin_Failure_WrongPassword() {
        // Un usuario previamente registrado en el sistema
        User user = new User();
        user.setName("Juan");
        user.setTax_id("JUAN900101ABC");
        user.setPhone("5512345678");
        user.setPassword("correct_pass");
        userService.saveUser(user);

        // Intento login con password incorrecto
        assertThrows(ResponseStatusException.class, () -> {
            userService.login("JUAN900101ABC", "wrong_pass");
        });
    }
}