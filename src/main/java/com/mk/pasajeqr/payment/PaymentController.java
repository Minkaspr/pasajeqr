package com.mk.pasajeqr.payment;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.payment.request.PaymentCreateRQ;
import com.mk.pasajeqr.payment.response.PaymentDetailRS;
import com.mk.pasajeqr.payment.response.PaymentsRS;
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
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaymentsRS>> listPayments(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        PaymentsRS result = paymentService.listAll(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lista de pagos", result, null)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDetailRS>> getPaymentById(
            @PathVariable @Min(value = 1, message = "El ID debe ser mayor a 0") Long id
    ) {
        PaymentDetailRS result = paymentService.getById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Pago encontrado", result, null)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDetailRS>> createPayment(
            @Valid @RequestBody PaymentCreateRQ request
    ) {
        PaymentDetailRS result = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Pago registrado exitosamente", result, null)
        );
    }
}
