package com.mk.pasajeqr.balance_transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Long> {
}
