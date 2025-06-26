package com.mk.pasajeqr.payment.response;

import com.mk.pasajeqr.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentItemRS {
    private Long id;
    private String userName;
    private String serviceCode;
    private String originStop;
    private String destinationStop;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;

    public PaymentItemRS(Payment payment) {
        this.id = payment.getId();
        this.userName = payment.getUser().getFirstName() + " " + payment.getUser().getLastName();
        this.serviceCode = payment.getService().getCode();
        this.originStop = payment.getOriginStop().getName();
        this.destinationStop = payment.getDestinationStop().getName();
        this.amount = payment.getAmount();
        this.paymentDate = payment.getPaymentDate();
        this.paymentMethod = payment.getPaymentMethod();
    }
}