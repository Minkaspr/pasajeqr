package com.mk.pasajeqr.payment;

import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.payment.request.PaymentCreateRQ;
import com.mk.pasajeqr.payment.response.PaymentDetailRS;
import com.mk.pasajeqr.payment.response.PaymentItemRS;
import com.mk.pasajeqr.payment.response.PaymentsRS;
import com.mk.pasajeqr.trip.TripRepository;
import com.mk.pasajeqr.trip.Trip;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.stop.StopRepository;
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
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private StopRepository stopRepository;

    @Override
    public PaymentsRS listAll(Pageable pageable) {
        Page<Payment> page = paymentRepository.findAll(pageable);
        List<PaymentItemRS> list = page.getContent().stream()
                .map(PaymentItemRS::new)
                .toList();

        return new PaymentsRS(list, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public PaymentDetailRS getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
        return new PaymentDetailRS(payment);
    }

    @Override
    @Transactional
    public PaymentDetailRS create(PaymentCreateRQ request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Trip service = tripRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        Stop origin = stopRepository.findById(request.getOriginStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de origen no encontrado"));

        Stop destination = stopRepository.findById(request.getDestinationStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de destino no encontrado"));

        Payment payment = Payment.builder()
                .user(user)
                .service(service)
                .originStop(origin)
                .destinationStop(destination)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentDate(LocalDateTime.now())
                .registeredBy(null) // Se puede ajustar según si hay un admin logueado
                .build();

        return new PaymentDetailRS(paymentRepository.save(payment));
    }
}
