package com.guadalupebecerril.user_api.service;

import com.guadalupebecerril.user_api.model.User;
import com.guadalupebecerril.user_api.model.Address;
import org.springframework.stereotype.Service;

import com.guadalupebecerril.user_api.util.ValidationUtil;
import com.guadalupebecerril.user_api.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.Comparator;

/**
 * Servicio encargado de la gestión integral de usuarios.
 * Implementa la lógica de persistencia en memoria, filtrado dinámico,
 * ordenamiento y procesos de autenticación.
 */
@Service
public class UserService {

    // Repositorio en memoria para el almacenamiento de usuarios.
    private final List<User> users = new ArrayList<>();

    // Contador atómico para garantizar IDs de dirección únicos y thread-safe.
    private final AtomicInteger addressGlobalCounter = new AtomicInteger(1);

    // Método para obtener la fecha en formato Madagascar
    public String getMadagascarTimestamp() {
        // Zona horaria de Madagascar: Indian/Antananarivo
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
        // Formato: dd-mm-yyyy HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return now.format(formatter);
    }

    public UserService() {
        // Inicialización de usuarios de prueba
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName("user" + i);
            user.setEmail("user" + i + "@mail.com");
            user.setPhone("555555555" + i);
            user.setTax_id("AARR990101XX" + i);
            user.setPassword(SecurityUtil.encrypt("password" + i));
            user.setCreated_at(getMadagascarTimestamp());

            Address address = new Address();
            address.setId(i);
            address.setName("Home " + i);
            address.setStreet("Street No. " + i);
            address.setCountryCode("MX");

            user.setAddresses(List.of(address));
            users.add(user);
        }
    }

    // Método para registrar un nuevo usuario aplicando reglas de validación
    public User saveUser(User newUser) {
        // Valida formato de RFC
        if (!ValidationUtil.isValidRFC(newUser.getTax_id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de RFC (tax_id) inválido.");
        }

        // Valida que el RFC sea único
        boolean exists = users.stream()
                .anyMatch(u -> u.getTax_id().equalsIgnoreCase(newUser.getTax_id()));
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tax_id ya existe.");
        }

        // Validar teléfono (10 dígitos)
        if (!ValidationUtil.isValidPhone(newUser.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El teléfono debe tener al menos 10 dígitos.");
        }

        // Generar IDs para las direcciones si existen
        if (newUser.getAddresses() != null) {
            for (Address addr : newUser.getAddresses()) {
                if (addr.getId() == null) {
                    addr.setId(addressGlobalCounter.getAndIncrement());
                }
            }
        }
        newUser.setId(UUID.randomUUID());
        newUser.setCreated_at(getMadagascarTimestamp());

        // Cifrado de la contraseña
        String encryptedPassword = SecurityUtil.encrypt(newUser.getPassword());
        newUser.setPassword(encryptedPassword);

        users.add(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return users;
    }

    // Filtra la lista de usuarios
    public List<User> filterUsers(String filter) {
        if (filter == null || filter.isEmpty()) {
            return users;
        }

        String[] conditions = filter.split(",");
        List<User> result = new ArrayList<>(users);

        for (String condition : conditions) {
            String[] parts = condition.trim().split(" ");
            if (parts.length < 3)
                continue;

            String field = parts[0];
            String operator = parts[1];
            String value = parts[2].toLowerCase();

            result = result.stream().filter(u -> {
                String fieldValue = "";
                String searchToCompare = value;

                // Determinar qué valor del objeto User vamos a comparar
                if (field.equals("name")) {
                    fieldValue = (u.getName() == null) ? "" : u.getName().toLowerCase();
                } else if (field.equals("email")) {
                    fieldValue = (u.getEmail() == null) ? "" : u.getEmail().toLowerCase();
                } else if (field.equals("tax_id")) {
                    fieldValue = (u.getTax_id() == null) ? "" : u.getTax_id().toLowerCase();
                } else if (field.equals("phone")) {
                    fieldValue = (u.getPhone() == null) ? "" : u.getPhone().replaceAll("[^0-9]", "");
                    searchToCompare = value.replaceAll("[^0-9]", "");
                }

                // Ejecuta la comparación con los valores ya procesados
                String finalFieldVal = fieldValue;
                String finalSearchVal = searchToCompare;

                return switch (operator) {
                    case "co" -> finalFieldVal.contains(finalSearchVal);
                    case "sw" -> finalFieldVal.startsWith(finalSearchVal);
                    case "ew" -> finalFieldVal.endsWith(finalSearchVal);
                    case "eq" -> finalFieldVal.equals(finalSearchVal);
                    default -> true;
                };
            }).toList();
        }
        return result;
    }

    // Ordena una lista de usuarios según el campo especificado.
    public List<User> sortUsers(List<User> userList, String sortedBy) {
        if (sortedBy == null || sortedBy.isBlank()) {
            return userList;
        }
        List<User> sortedList = new ArrayList<>(userList);

        Comparator<User> comparator = switch (sortedBy.toLowerCase()) {
            case "email" -> Comparator.comparing(User::getEmail, Comparator.nullsLast(String::compareTo));
            case "name" -> Comparator.comparing(User::getName, Comparator.nullsLast(String::compareTo));
            case "phone" -> Comparator.comparing(User::getPhone, Comparator.nullsLast(String::compareTo));
            case "tax_id" -> Comparator.comparing(User::getTax_id, Comparator.nullsLast(String::compareTo));
            case "created_at" -> Comparator.comparing(User::getCreated_at, Comparator.nullsLast(String::compareTo));
            case "id" -> Comparator.comparing(user -> user.getId().toString());
            default -> null;
        };

        if (comparator != null) {
            sortedList.sort(comparator);
        }

        return sortedList;
    }

    // Actualizar un atributo por ID
    public User updateUser(UUID id, User updateData) {
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Solo actualizamos los campos que no sean nulos
        if (updateData.getName() != null)
            user.setName(updateData.getName());
        if (updateData.getEmail() != null)
            user.setEmail(updateData.getEmail());
        if (updateData.getPhone() != null)
            user.setPhone(updateData.getPhone());
        if (updateData.getTax_id() != null)
            user.setTax_id(updateData.getTax_id());

        return user;
    }

    // Eliminar un usuario por ID
    public void deleteUser(UUID id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo eliminar: Usuario no encontrado");
        }
    }

    // Valida las credenciales de acceso de un usuario mediante el tax_id y su
    // password
    public User login(String taxId, String password) {
        User user = users.stream()
                .filter(u -> u.getTax_id().equalsIgnoreCase(taxId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        String encryptedInput = SecurityUtil.encrypt(password);

        if (user.getPassword().equals(encryptedInput)) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
    }
}