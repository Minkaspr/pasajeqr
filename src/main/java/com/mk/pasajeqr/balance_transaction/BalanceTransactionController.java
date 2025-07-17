package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.balance_transaction.request.BalanceTransactionCreateRQ;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionDetailRS;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionsRS;
import com.mk.pasajeqr.balance_transaction.response.DailyTransactionSummaryRS;
import com.mk.pasajeqr.common.response.ApiResponse;
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
@RequestMapping("/api/v1/transactions")
public class BalanceTransactionController {

    @Autowired
    private BalanceTransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<BalanceTransactionsRS>> listPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        BalanceTransactionsRS result = transactionService.listPaged(pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Historial de transacciones", result, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BalanceTransactionDetailRS>> getById(
            @PathVariable @Min(1) Long id
    ) {
        BalanceTransactionDetailRS result = transactionService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Transacción encontrada", result, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BalanceTransactionDetailRS>> create(
            @Valid @RequestBody BalanceTransactionCreateRQ request
    ) {
        BalanceTransactionDetailRS result = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Transacción registrada exitosamente", result, null));
    }

    @GetMapping("/summary/last-7-days")
    public ResponseEntity<ApiResponse<DailyTransactionSummaryRS>> getLast7DaysSummary() {
        DailyTransactionSummaryRS summary = transactionService.getLast7DaysSummary();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Resumen de los últimos 7 días obtenido correctamente",
                        summary,
                        null
                )
        );
    }
}
