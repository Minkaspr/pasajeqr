package com.mk.pasajeqr.balance_transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceTransactionsRS {
    private List<BalanceTransactionItemRS> transactions;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
