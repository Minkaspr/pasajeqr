package com.mk.pasajeqr.passenger.response;

import com.mk.pasajeqr.utils.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailRS {
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
}