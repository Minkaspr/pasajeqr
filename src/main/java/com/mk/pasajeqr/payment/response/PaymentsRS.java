package com.mk.pasajeqr.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsRS {
    private List<PaymentItemRS> payments;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
