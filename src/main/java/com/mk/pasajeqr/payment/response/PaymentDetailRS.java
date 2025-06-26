package com.mk.pasajeqr.payment.response;

import com.mk.pasajeqr.payment.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDetailRS {
    private Long id;
    private String userFullName;
    private String serviceCode;
    private String originStop;
    private String destinationStop;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String registeredBy;

    public PaymentDetailRS(Payment payment) {
        this.id = payment.getId();
        this.userFullName = payment.getUser().getFirstName() + " " + payment.getUser().getLastName();
        this.serviceCode = payment.getService().getCode();
        this.originStop = payment.getOriginStop().getName();
        this.destinationStop = payment.getDestinationStop().getName();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentDate = payment.getPaymentDate();
        this.registeredBy = payment.getRegisteredBy().getFirstName() + " " + payment.getRegisteredBy().getLastName();
    }
}
