package com.guadalupebecerril.user_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guadalupebecerril.user_api.model.User;
import com.guadalupebecerril.user_api.service.UserService;

/**
 * Controlador REST para servicios de gestión de usuarios.
 * Provee puntos de acceso para operaciones CRUD, incluyendo capacidades
 * de filtrado dinámico y ordenamiento.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Crea un nuevo usuario en el sistema.
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Recupera una lista de usuarios con opciones de búsqueda y orden.
    @GetMapping
    public List<User> getUsers(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sortedBy) {

        // Se procesa primero el filtrado de datos
        List<User> filtered = userService.filterUsers(filter);

        // Se aplica el ordenamiento sobre el conjunto de datos filtrado
        return userService.sortUsers(filtered, sortedBy);
    }

    // Actualiza parcialmente la información de un usuario existente.
    @PatchMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Elimina de forma definitiva a un usuario del sistema.
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
