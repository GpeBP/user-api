package com.guadalupebecerril.user_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Representa la información del domicilio de un usuario.
 * Esta clase se utiliza para el mapeo de datos en la API de usuarios.
 */
@Data
public class Address {
    private Integer id;
    private String name;
    private String street;
    @JsonProperty("country_code")
    private String countryCode;
}