package com.guadalupebecerril.user_api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Representa la información del domicilio de un usuario.
 * Esta clase se utiliza para el mapeo de datos en la API de usuarios.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Address {
    private Integer id;
    private String name;
    private String street;
    private String countryCode;
}