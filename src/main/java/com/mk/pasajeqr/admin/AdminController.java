package com.mk.pasajeqr.admin;

import com.mk.pasajeqr.admin.request.AdminCreateRQ;
import com.mk.pasajeqr.admin.request.AdminUpdateRQ;
import com.mk.pasajeqr.admin.response.AdminDetailRS;
import com.mk.pasajeqr.admin.response.AdminsRS;
import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.utils.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminsRS>> getAllPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        AdminsRS result = adminService.getAllPaged(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de administradores", result, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminDetailRS>> create(
            @Valid @RequestBody AdminCreateRQ request
    ) {
        AdminDetailRS createdAdmin = adminService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Administrador creado exitosamente", createdAdmin, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminDetailRS>> getById(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        AdminDetailRS admin = adminService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Administrador encontrado", admin, null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminDetailRS>> update(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody AdminUpdateRQ request
    ) {
        AdminDetailRS updatedAdmin = adminService.update(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Administrador actualizado correctamente", updatedAdmin, null)
        );
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody ChangePasswordRQ request
    ) {
        adminService.changePassword(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Contraseña actualizada con éxito", null, null)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> changeStatus(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody ChangeStatusRQ request
    ) {
        UserStatusRS response = adminService.setUserStatus(id, request.getActive());

        String message = request.getActive() ? "Cuenta activada con éxito" : "Cuenta desactivada con éxito";

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), message, response, null)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = adminService.deleteBulk(request.getIds());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Operación de eliminación masiva completada", result, null)
        );
    }
}
