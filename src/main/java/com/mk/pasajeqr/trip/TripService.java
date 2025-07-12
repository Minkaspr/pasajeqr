package com.mk.pasajeqr.trip;

import com.mk.pasajeqr.trip.request.TripCreateRQ;
import com.mk.pasajeqr.trip.request.TripUpdateRQ;
import com.mk.pasajeqr.trip.response.TripDetailRS;
import com.mk.pasajeqr.trip.response.TripEditRS;
import com.mk.pasajeqr.trip.response.TripRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TripService {
    TripRS listPaged(Pageable pageable, String codeFilter);
    TripDetailRS create(TripCreateRQ request);
    TripEditRS getById(Long id);
    TripDetailRS update(Long id, TripUpdateRQ request);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
    String generateQrToken(Long tripId);
}