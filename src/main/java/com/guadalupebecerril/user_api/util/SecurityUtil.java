package com.guadalupebecerril.user_api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Componente de utilería para operaciones criptográficas de la aplicación.
 * Proporciona métodos para el cifrado y descifrado de información sensible
 * utilizando el algoritmo AES.
 */
public class SecurityUtil {
    // Llave de cifrado simétrico (256 bits).
    private static final String SECRET_KEY = "Test_Gb_Secret_Key_2026_Java_Api";

    public static String encrypt(String strToEncrypt) {
        try {
            // Configuración de la llave simétrica bajo el algoritmo AES
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            // Instancia del cifrador con transformación AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            // Excepción con un mensaje contextual para facilitar el debugging
            throw new RuntimeException("Error al cifrar la contraseña: " + e.getMessage());
        }
    }
}