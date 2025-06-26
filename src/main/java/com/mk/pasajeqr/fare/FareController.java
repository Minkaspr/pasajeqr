package com.mk.pasajeqr.fare;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.fare.request.FareCreateRQ;
import com.mk.pasajeqr.fare.request.FareUpdateRQ;
import com.mk.pasajeqr.fare.response.FareDetailRS;
import com.mk.pasajeqr.fare.response.FaresRS;
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
@RequestMapping("/api/v1/fares")
public class FareController {

    @Autowired
    private FareService fareService;

    @GetMapping
    public ResponseEntity<ApiResponse<FaresRS>> listPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        FaresRS result = fareService.listPaged(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de tarifas", result, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FareDetailRS>> create(
            @Valid @RequestBody FareCreateRQ request
    ) {
        FareDetailRS createdFare = fareService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Tarifa creada exitosamente", createdFare, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FareDetailRS>> getById(
            @PathVariable @Min(1) Long id
    ) {
        FareDetailRS fare = fareService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Tarifa encontrada", fare, null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FareDetailRS>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody FareUpdateRQ request
    ) {
        FareDetailRS updatedFare = fareService.update(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Tarifa actualizada exitosamente", updatedFare, null)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id
    ) {
        fareService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = fareService.deleteBulk(request.getIds());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Eliminación masiva completada", result, null)
        );
    }
}
