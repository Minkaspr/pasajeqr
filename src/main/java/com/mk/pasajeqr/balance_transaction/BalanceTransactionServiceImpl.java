package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.balance_transaction.request.BalanceTransactionCreateRQ;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionDetailRS;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionItemRS;
import com.mk.pasajeqr.balance_transaction.response.BalanceTransactionsRS;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
}
