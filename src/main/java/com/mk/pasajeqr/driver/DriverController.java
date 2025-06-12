package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.driver.request.BulkDeleteRequest;
import com.mk.pasajeqr.driver.request.ChangeStatusRequest;
import com.mk.pasajeqr.driver.request.DriverRegisterRequest;
import com.mk.pasajeqr.driver.request.DriverUpdateRequest;
import com.mk.pasajeqr.driver.response.BulkDeleteResponseDTO;
import com.mk.pasajeqr.driver.response.DriverDetailDTO;
import com.mk.pasajeqr.driver.response.DriversResponseDTO;
import com.mk.pasajeqr.user.dto.UserStatusResponse;
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
    public ResponseEntity<ApiResponse<DriverDetailDTO>> createDriver(@Valid @RequestBody DriverRegisterRequest request) {
        DriverDetailDTO dto = driverService.createDriver(request);
        ApiResponse<DriverDetailDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Driver creado exitosamente",
                dto,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener driver por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDetailDTO>> getDriverById(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        DriverDetailDTO dto = driverService.getById(id);

        ApiResponse<DriverDetailDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Driver encontrado",
                dto,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Listar drivers con paginación
    @GetMapping
    public ResponseEntity<ApiResponse<DriversResponseDTO>> listDrivers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        DriversResponseDTO responseDTO = driverService.listDrivers(pageable);

        ApiResponse<DriversResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de drivers",
                responseDTO,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Actualizar driver
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDetailDTO>> updateDriver(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody DriverUpdateRequest request
    ) {
        DriverDetailDTO dto = driverService.updateDriver(id, request);
        ApiResponse<DriverDetailDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Driver actualizado correctamente",
                dto,
                null
        );
        return ResponseEntity.ok(response);
    }

    // Actualizar el status driver por ID
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> changeDriverStatus(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody ChangeStatusRequest request
    ) {
        UserStatusResponse response = driverService.setUserStatus(id, request.getActive());
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
    public ResponseEntity<ApiResponse<BulkDeleteResponseDTO>> deleteMultipleDrivers(@Valid @RequestBody BulkDeleteRequest request) {
        BulkDeleteResponseDTO result = driverService.deleteDrivers(request.getDriverIds());

        String message = "Operación de eliminación masiva completada";
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), message, result, null)
        );
    }
}
