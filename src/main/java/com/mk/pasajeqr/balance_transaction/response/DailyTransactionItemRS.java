package com.mk.pasajeqr.balance_transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTransactionItemRS {
    private LocalDate date;       // e.g. 2025-07-17
    private BigDecimal recargas;  // suma de RECHARGE ese día
    private BigDecimal pagos;     // suma de PAYMENT ese día
}