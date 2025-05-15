package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transaction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
}
