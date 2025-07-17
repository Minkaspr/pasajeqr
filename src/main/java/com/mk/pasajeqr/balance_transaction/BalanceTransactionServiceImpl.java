package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.balance_transaction.request.BalanceTransactionCreateRQ;
import com.mk.pasajeqr.balance_transaction.response.*;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.utils.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BalanceTransactionServiceImpl implements BalanceTransactionService{

    @Autowired
    private BalanceTransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BalanceTransactionsRS listPaged(Pageable pageable) {
        Page<BalanceTransaction> page = transactionRepository.findAll(pageable);
        List<BalanceTransactionItemRS> items = page.getContent().stream()
                .map(BalanceTransactionItemRS::new)
                .toList();

        return new BalanceTransactionsRS(items, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public BalanceTransactionDetailRS getById(Long id) {
        BalanceTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transacción no encontrada con ID: " + id));
        return new BalanceTransactionDetailRS(transaction);
    }

    @Override
    @Transactional
    public BalanceTransactionDetailRS create(BalanceTransactionCreateRQ request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));

        BalanceTransaction transaction = BalanceTransaction.builder()
                .user(user)
                .type(request.getType())
                .amount(request.getAmount())
                .transactionDate(LocalDateTime.now())
                .description(request.getDescription())
                .build();

        BalanceTransaction saved = transactionRepository.save(transaction);
        return new BalanceTransactionDetailRS(saved);
    }

    @Override
    public DailyTransactionSummaryRS getLast7DaysSummary() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6); // Incluye hoy y los 6 anteriores

        LocalDateTime startDateTime = sevenDaysAgo.atStartOfDay();
        LocalDateTime endDateTime = today.plusDays(1).atStartOfDay().minusNanos(1); // Hasta el final de hoy

        List<BalanceTransaction> transactions = transactionRepository
                .findByTransactionDateBetween(startDateTime, endDateTime);

        // Agrupar por día
        Map<LocalDate, DailyTransactionItemRS> summaryMap = new TreeMap<>(); // orden cronológico

        for (BalanceTransaction tx : transactions) {
            LocalDate txDate = tx.getTransactionDate().toLocalDate();
            DailyTransactionItemRS daily = summaryMap.getOrDefault(txDate, new DailyTransactionItemRS(txDate, BigDecimal.ZERO, BigDecimal.ZERO));

            if (tx.getType() == TransactionType.RECHARGE) {
                daily.setRecargas(daily.getRecargas().add(tx.getAmount()));
            } else if (tx.getType() == TransactionType.PAYMENT) {
                daily.setPagos(daily.getPagos().add(tx.getAmount()));
            }

            summaryMap.put(txDate, daily);
        }

        // Asegurar que todos los días existan (aunque sea en 0)
        List<DailyTransactionItemRS> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = sevenDaysAgo.plusDays(i);
            result.add(summaryMap.getOrDefault(date, new DailyTransactionItemRS(date, BigDecimal.ZERO, BigDecimal.ZERO)));
        }

        return new DailyTransactionSummaryRS(result);
    }
}
