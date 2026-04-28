package com.guadalupebecerril.user_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
    // -------------------------------------------------------------------------
    // saveUser
    // -------------------------------------------------------------------------

    @Test
    void saveUser_validData_assignsIdAndTimestamp() {
        User user = buildUser("VAMI920425ABC", "5512345678");

        User saved = userService.saveUser(user);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertEquals("Test User", saved.getName());
    }

    @Test
    void saveUser_passwordIsHashed() {
        User user = buildUser("VAMI920425ABC", "5512345678");
        user.setPassword("secret123");

        User saved = userService.saveUser(user);

        assertNotEquals("secret123", saved.getPassword());
    }

    @Test
    void saveUser_invalidRfc_throwsBadRequest() {
        User user = new User();
        user.setTaxId("RFC-INVALIDO");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.saveUser(user));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void saveUser_duplicateRfc_throwsBadRequest() {
        userService.saveUser(buildUser("VAMI920425ABC", "5512345678"));

        User duplicate = buildUser("VAMI920425ABC", "5599999999");
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.saveUser(duplicate));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void saveUser_invalidPhone_throwsBadRequest() {
        User user = buildUser("VAMI920425ABC", "123");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.saveUser(user));
        assertEquals(400, ex.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // login
    // -------------------------------------------------------------------------

    @Test
    void login_correctCredentials_returnsUser() {
        User user = buildUser("LORE800101XYZ", "5512345678");
        user.setPassword("mypassword");
        userService.saveUser(user);

        User result = userService.login("LORE800101XYZ", "mypassword");

        assertEquals("LORE800101XYZ", result.getTaxId());
    }

    @Test
    void login_wrongPassword_throwsUnauthorized() {
        User user = buildUser("JUAN900101ABC", "5512345678");
        user.setPassword("correct_pass");
        userService.saveUser(user);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.login("JUAN900101ABC", "wrong_pass"));
        assertEquals(401, ex.getStatusCode().value());
    }

    @Test
    void login_unknownTaxId_throwsUnauthorized() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.login("XXXX000000XXX", "any"));
        assertEquals(401, ex.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // filterUsers
    // -------------------------------------------------------------------------

    @Test
    void filterUsers_byNameContains_returnsMatch() {
        List<User> result = userService.filterUsers("name co user1");

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getName());
    }

    @Test
    void filterUsers_byEmailEndsWith_returnsMatches() {
        List<User> result = userService.filterUsers("email ew mail.com");

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(u -> u.getEmail().endsWith("mail.com")));
    }

    @Test
    void filterUsers_nullFilter_returnsAll() {
        List<User> result = userService.filterUsers(null);

        assertEquals(3, result.size());
    }

    @Test
    void filterUsers_unknownField_returnsAll() {
        List<User> result = userService.filterUsers("unknown eq value");

        assertEquals(0, result.size());
    }

    // -------------------------------------------------------------------------
    // sortUsers
    // -------------------------------------------------------------------------

    @Test
    void sortUsers_byName_returnsAlphabeticalOrder() {
        List<User> all = userService.getAllUsers();
        List<User> sorted = userService.sortUsers(all, "name");

        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getName().compareTo(sorted.get(i + 1).getName()) <= 0);
        }
    }

    @Test
    void sortUsers_nullSortedBy_returnsSameList() {
        List<User> all = userService.getAllUsers();
        List<User> result = userService.sortUsers(all, null);

        assertEquals(all.size(), result.size());
    }

    // -------------------------------------------------------------------------
    // updateUser
    // -------------------------------------------------------------------------

    @Test
    void updateUser_existingUser_updatesFields() {
        User existing = userService.getAllUsers().get(0);

        User update = new User();
        update.setEmail("nuevo@mail.com");

        User updated = userService.updateUser(existing.getId(), update);

        assertEquals("nuevo@mail.com", updated.getEmail());
    }

    @Test
    void updateUser_nonExistentId_throwsNotFound() {
        User update = new User();
        update.setEmail("x@mail.com");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(java.util.UUID.randomUUID(), update));
        assertEquals(404, ex.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // deleteUser
    // -------------------------------------------------------------------------

    @Test
    void deleteUser_existingUser_removesFromList() {
        User existing = userService.getAllUsers().get(0);
        int sizeBefore = userService.getAllUsers().size();

        userService.deleteUser(existing.getId());

        assertEquals(sizeBefore - 1, userService.getAllUsers().size());
    }

    @Test
    void deleteUser_nonExistentId_throwsNotFound() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.deleteUser(java.util.UUID.randomUUID()));
        assertEquals(404, ex.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private User buildUser(String taxId, String phone) {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setTaxId(taxId);
        user.setPhone(phone);
        user.setPassword("password123");
        return user;
    }
}
