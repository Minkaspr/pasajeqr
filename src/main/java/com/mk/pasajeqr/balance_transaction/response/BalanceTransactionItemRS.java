package com.mk.pasajeqr.balance_transaction.response;

import com.mk.pasajeqr.balance_transaction.BalanceTransaction;
import com.mk.pasajeqr.utils.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceTransactionItemRS {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public BalanceTransactionItemRS(BalanceTransaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.transactionDate = transaction.getTransactionDate();
    }
}
