package com.mk.pasajeqr.user;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.user.request.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    // Crear usuario
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserRegisterRequest request) {
        User nuevoUser = userService.createUser(request);
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario creado exitosamente",
                Map.of("user", nuevoUser),
                null
        );

        return ResponseEntity.ok(response);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar usuario por email (opcional)
    @GetMapping("/buscar")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
