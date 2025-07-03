package com.mk.pasajeqr.bus;

import com.mk.pasajeqr.bus.request.BusCreateRQ;
import com.mk.pasajeqr.bus.request.BusUpdateRQ;
import com.mk.pasajeqr.bus.response.BusDetailRS;
import com.mk.pasajeqr.bus.response.BusesRS;
import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.utils.BulkDeleteRQ;
import com.mk.pasajeqr.utils.BulkDeleteRS;
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
@RequestMapping("/api/v1/buses")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping
    public ResponseEntity<ApiResponse<BusesRS>> listPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        BusesRS result = busService.listPaged(pageable, search);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lista de buses", result, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BusDetailRS>> create(@Valid @RequestBody BusCreateRQ request) {
        BusDetailRS created = busService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Bus creado exitosamente", created, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDetailRS>> getById(
            @PathVariable @Min(1) Long id
    ) {
        BusDetailRS result = busService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Bus encontrado", result, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusDetailRS>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody BusUpdateRQ request
    ) {
        BusDetailRS updated = busService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Bus actualizado correctamente", updated, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        busService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = busService.deleteBulk(request.getIds());
        return ResponseEntity.ok(new ApiResponse<>(200, "Eliminación masiva completada", result, null));
    }
}
