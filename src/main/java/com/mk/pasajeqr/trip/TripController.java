package com.mk.pasajeqr.trip;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.trip.request.TripCreateRQ;
import com.mk.pasajeqr.trip.request.TripUpdateRQ;
import com.mk.pasajeqr.trip.response.TripDetailRS;
import com.mk.pasajeqr.trip.response.TripEditRS;
import com.mk.pasajeqr.trip.response.TripRS;
import com.mk.pasajeqr.utils.BulkDeleteRQ;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/services")
@Validated
public class TripController {

    @Autowired
    private TripService serviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<TripRS>> getAllPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String code
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureDate").descending().and(Sort.by("departureTime")));
        TripRS result = serviceService.listPaged(pageable, code);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de servicios", result, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripDetailRS>> create(
            @Valid @RequestBody TripCreateRQ request
    ) {
        TripDetailRS created = serviceService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Servicio creado exitosamente", created, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripEditRS>> getById(
            @PathVariable @Min(1) Long id
    ) {
        TripEditRS detail = serviceService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Servicio encontrado", detail, null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripDetailRS>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody TripUpdateRQ request
    ) {
        TripDetailRS updated = serviceService.update(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Servicio actualizado exitosamente", updated, null)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id
    ) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = serviceService.deleteBulk(request.getIds());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Eliminación masiva completada", result, null)
        );
    }

    @GetMapping("/{id}/qr-token")
    public ResponseEntity<ApiResponse<Map<String, String>>> getQrToken(
            @PathVariable @Min(1) Long id
    ) {
        String token = serviceService.generateQrToken(id);
        Map<String, String> response = Map.of("token", token);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Token generado exitosamente", response, null)
        );
    }

    @GetMapping("/validate-qr")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateQrToken(
            @RequestParam String token
    ) {
        Map<String, Object> result = serviceService.validateQrToken(token);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "QR validado correctamente",
                        result,
                        null
                )
        );
    }
}