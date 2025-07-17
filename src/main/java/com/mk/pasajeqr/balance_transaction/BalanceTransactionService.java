package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.balance_transaction.request.BalanceTransactionCreateRQ;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionDetailRS;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionsRS;
import com.mk.pasajeqr.balance_transaction.response.DailyTransactionSummaryRS;
import org.springframework.data.domain.Pageable;

public interface BalanceTransactionService {
    BalanceTransactionsRS listPaged(Pageable pageable);
    BalanceTransactionDetailRS getById(Long id);
    BalanceTransactionDetailRS create(BalanceTransactionCreateRQ request);
    DailyTransactionSummaryRS getLast7DaysSummary();
}
