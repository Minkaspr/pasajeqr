package com.mk.pasajeqr.payment;

import com.mk.pasajeqr.payment.request.PaymentCreateRQ;
import com.mk.pasajeqr.payment.response.PaymentDetailRS;
import com.mk.pasajeqr.payment.response.PaymentsRS;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentsRS listAll(Pageable pageable);
    PaymentDetailRS getById(Long id);
    PaymentDetailRS create(PaymentCreateRQ request);
}
