package com.guadalupebecerril.user_api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Componente de utilería para operaciones criptográficas de la aplicación.
 */
public class SecurityUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}