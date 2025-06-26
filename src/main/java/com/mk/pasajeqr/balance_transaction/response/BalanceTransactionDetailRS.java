package com.mk.pasajeqr.balance_transaction.response;

import com.mk.pasajeqr.balance_transaction.BalanceTransaction;
import com.mk.pasajeqr.utils.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceTransactionDetailRS {
    private Long id;
    private Long userId;
    private String userFullName;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;

    public BalanceTransactionDetailRS(BalanceTransaction transaction) {
        this.id = transaction.getId();
        this.userId = transaction.getUser().getId();
        this.userFullName = transaction.getUser().getFirstName() + " " + transaction.getUser().getLastName();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
    }
}
