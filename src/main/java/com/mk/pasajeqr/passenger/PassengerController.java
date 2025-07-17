package com.mk.pasajeqr.passenger;

import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionDetailRS;
import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.passenger.request.FarePaymentRQ;
import com.mk.pasajeqr.passenger.request.PassengerCreateRQ;
import com.mk.pasajeqr.passenger.request.PassengerUpdateRQ;
import com.mk.pasajeqr.passenger.request.RechargeRQ;
import com.mk.pasajeqr.passenger.response.PassengerBalanceHistoryRS;
import com.mk.pasajeqr.passenger.response.PassengerDetailRS;
import com.mk.pasajeqr.passenger.response.PassengersRS;
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
@RequestMapping("/api/v1/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PassengersRS>> getAllPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        PassengersRS result = passengerService.getAllPaged(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de pasajeros", result, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PassengerDetailRS>> create(
            @Valid @RequestBody PassengerCreateRQ request
    ) {
        PassengerDetailRS createdPassenger = passengerService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Pasajero creado exitosamente", createdPassenger, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PassengerDetailRS>> getById(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        PassengerDetailRS passenger = passengerService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Pasajero encontrado", passenger, null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PassengerDetailRS>> update(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody PassengerUpdateRQ request
    ) {
        PassengerDetailRS updatedPassenger = passengerService.update(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Pasajero actualizado correctamente", updatedPassenger, null)
        );
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody ChangePasswordRQ request
    ) {
        passengerService.changePassword(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Contraseña actualizada con éxito", null, null)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> changeStatus(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id,
            @Valid @RequestBody UserStatusRQ request
    ) {
        UserStatusRS response = passengerService.setUserStatus(id, request.getActive());

        String message = request.getActive() ? "Cuenta activada con éxito" : "Cuenta desactivada con éxito";

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), message, response, null)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<BulkDeleteRS>> deleteBulk(
            @Valid @RequestBody BulkDeleteRQ request
    ) {
        BulkDeleteRS result = passengerService.deleteBulk(request.getIds());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Operación de eliminación masiva completada", result, null)
        );
    }

    @PostMapping("/{id}/recharge")
    public ResponseEntity<ApiResponse<BalanceTransactionDetailRS>> rechargeBalance(
            @PathVariable Long id,
            @Valid @RequestBody RechargeRQ request
    ) {
        BalanceTransactionDetailRS tx = passengerService.recharge(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Recarga realizada correctamente", tx, null)
        );
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<BalanceTransactionDetailRS>> payFare(
            @PathVariable Long id,
            @Valid @RequestBody FarePaymentRQ request
    ) {
        BalanceTransactionDetailRS tx = passengerService.payFare(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Pago realizado correctamente", tx, null)
        );
    }

    @GetMapping("/{id}/balance-history")
    public ResponseEntity<ApiResponse<PassengerBalanceHistoryRS>> getBalanceWithHistory(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor o igual a 1") Long id
    ) {
        PassengerBalanceHistoryRS result = passengerService.getBalanceWithHistory(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Saldo e historial de transacciones recuperados correctamente",
                        result,
                        null
                )
        );
    }
}
