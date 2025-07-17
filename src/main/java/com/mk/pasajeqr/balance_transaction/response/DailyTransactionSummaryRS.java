package com.mk.pasajeqr.balance_transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTransactionSummaryRS {
    private List<DailyTransactionItemRS> data;
}