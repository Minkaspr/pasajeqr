package com.mk.pasajeqr.stop;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.stop.request.StopCreateRQ;
import com.mk.pasajeqr.stop.request.StopUpdateRQ;
import com.mk.pasajeqr.stop.response.StopDetailRS;
import com.mk.pasajeqr.stop.response.StopsRS;
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
@RequestMapping("/api/v1/stops")
public class StopController {

    @Autowired
    private StopService stopService;

    @GetMapping
    public ResponseEntity<ApiResponse<StopsRS>> getAllPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        StopsRS stops = stopService.listPaged(search, pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lista de paraderos", stops, null));
    }

    @GetMapping("/terminals")
    public ResponseEntity<ApiResponse<StopsRS>> getTerminalStops(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        StopsRS stops = stopService.listTerminalStops(pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de paraderos terminales", stops, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StopDetailRS>> create(@Valid @RequestBody StopCreateRQ request) {
        StopDetailRS created = stopService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Paradero creado exitosamente", created, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StopDetailRS>> getById(
            @PathVariable @Min(1) Long id
    ) {
        StopDetailRS stop = stopService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Paradero encontrado", stop, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StopDetailRS>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody StopUpdateRQ request
    ) {
        StopDetailRS updated = stopService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Paradero actualizado correctamente", updated, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
        stopService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = stopService.deleteBulk(request.getIds());
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Eliminación masiva completada", result, null)
        );
    }
}

