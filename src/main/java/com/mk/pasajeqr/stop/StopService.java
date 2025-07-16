package com.mk.pasajeqr.stop;

import com.mk.pasajeqr.stop.request.StopCreateRQ;
import com.mk.pasajeqr.stop.request.StopUpdateRQ;
import com.mk.pasajeqr.stop.response.StopDetailRS;
import com.mk.pasajeqr.stop.response.StopsRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StopService {
    StopsRS listPaged(String search, Pageable pageable);
    StopsRS listTerminalStops(Pageable pageable);
    StopDetailRS create(StopCreateRQ request);
    StopDetailRS getById(Long id);
    StopDetailRS update(Long id, StopUpdateRQ request);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
}
