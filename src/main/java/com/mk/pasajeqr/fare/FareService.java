package com.mk.pasajeqr.fare;

import com.mk.pasajeqr.fare.request.FareCreateRQ;
import com.mk.pasajeqr.fare.request.FareUpdateRQ;
import com.mk.pasajeqr.fare.response.FareDetailRS;
import com.mk.pasajeqr.fare.response.FaresRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FareService {
    FaresRS listPaged(String search, Pageable pageable);
    FareDetailRS create(FareCreateRQ request);
    FareDetailRS getById(Long id);
    FareDetailRS update(Long id, FareUpdateRQ request);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
}

