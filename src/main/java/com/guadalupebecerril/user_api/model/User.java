package com.guadalupebecerril.user_api.model;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Entidad principal que representa a un usuario
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    private UUID id;
    private String email;
    private String name;
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // WRITE_ONLY para que no se incluya en las respuestas JSON
    private String password;

    private String taxId; // RFC
    private String createdAt; // Formato Madagascar
    private List<Address> addresses;
}