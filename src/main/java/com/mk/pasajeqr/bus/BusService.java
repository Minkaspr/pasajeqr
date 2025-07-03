package com.mk.pasajeqr.bus;

import com.mk.pasajeqr.bus.request.BusCreateRQ;
import com.mk.pasajeqr.bus.request.BusUpdateRQ;
import com.mk.pasajeqr.bus.response.BusDetailRS;
import com.mk.pasajeqr.bus.response.BusesRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusService {
    BusesRS listPaged(Pageable pageable, String search);
    BusDetailRS create(BusCreateRQ request);
    BusDetailRS getById(Long id);
    BusDetailRS update(Long id, BusUpdateRQ request);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
}
