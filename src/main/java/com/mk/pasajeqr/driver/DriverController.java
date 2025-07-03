package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.utils.*;
import com.mk.pasajeqr.driver.request.DriverCreateRQ;
import com.mk.pasajeqr.driver.request.DriverUpdateRQ;
import com.mk.pasajeqr.driver.response.DriverDetailRS;
import com.mk.pasajeqr.driver.response.DriversRS;
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
@RequestMapping("/api/v1/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    // Crear un driver
    @PostMapping
    public ResponseEntity<ApiResponse<DriverDetailRS>> createDriver(
            @Valid @RequestBody DriverCreateRQ request
    ) {
        DriverDetailRS dto = driverService.createDriver(request);
        ApiResponse<DriverDetailRS> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Driver creado exitosamente",
                dto,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener driver por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDetailRS>> getDriverById(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        DriverDetailRS dto = driverService.getById(id);

        ApiResponse<DriverDetailRS> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Driver encontrado",
                dto,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Listar drivers con paginación
    @GetMapping
    public ResponseEntity<ApiResponse<DriversRS>> listDrivers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        DriversRS result = driverService.listDrivers(pageable);

        ApiResponse<DriversRS> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de drivers",
                result,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Actualizar driver
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDetailRS>> updateDriver(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody DriverUpdateRQ request
    ) {
        DriverDetailRS updateDriver = driverService.updateDriver(id, request);
        ApiResponse<DriverDetailRS> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Driver actualizado correctamente",
                updateDriver,
                null
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody ChangePasswordRQ request
    ) {
        driverService.changePassword(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Contraseña actualizada con éxito", null, null)
        );
    }

    // Actualizar el status driver por ID
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> changeDriverStatus(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody UserStatusRQ request
    ) {
        UserStatusRS response = driverService.setUserStatus(id, request.getActive());
        String message = request.getActive() ? "Cuenta activada con éxito" : "Cuenta desactivada con éxito";
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), message, response, null));
    }

    // Eliminar driver
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteMultipleDrivers(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = driverService.deleteDrivers(request.getIds());

        String message = "Operación de eliminación masiva completada";
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), message, result, null)
        );
    }
}
