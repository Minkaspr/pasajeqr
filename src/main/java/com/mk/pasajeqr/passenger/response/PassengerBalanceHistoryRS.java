package com.mk.pasajeqr.passenger.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerBalanceHistoryRS {
    private BigDecimal currentBalance;
    private List<TransactionDetailRS> transactions;
}
