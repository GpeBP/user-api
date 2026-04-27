package com.guadalupebecerril.user_api.util;

/**
 * Clase utilitaria para la validación de formatos de datos de entrada.
 */
public class ValidationUtil {

    // Validación de RFC
    public static boolean isValidRFC(String rfc) {
        if (rfc == null)
            return false;
        String rfcPattern = "^[A-Z&Ñ]{3,4}\\d{6}[A-Z0-9]{3}$";
        return rfc.toUpperCase().matches(rfcPattern);
    }

    // Validación "AndresFormat" para el teléfono
    public static boolean isValidPhone(String phone) {
        if (phone == null)
            return false;
        // Se eliminan caracteres especiales para validar únicamente la carga numérica
        String digits = phone.replaceAll("[^0-9]", "");
        // Verifica que tenga exactamente 10 dígitos (o al menos 10 si incluyes país)
        return digits.length() >= 10;
    }
}