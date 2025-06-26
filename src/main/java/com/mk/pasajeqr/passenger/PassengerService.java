package com.mk.pasajeqr.passenger;

import com.mk.pasajeqr.passenger.request.PassengerCreateRQ;
import com.mk.pasajeqr.passenger.request.PassengerUpdateRQ;
import com.mk.pasajeqr.passenger.response.PassengerDetailRS;
import com.mk.pasajeqr.passenger.response.PassengersRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.UserStatusRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PassengerService {
    PassengersRS getAllPaged(Pageable pageable);
    PassengerDetailRS create(PassengerCreateRQ request);
    PassengerDetailRS getById(Long id);
    PassengerDetailRS update(Long id, PassengerUpdateRQ request);
    void changePassword(Long id, ChangePasswordRQ request);
    UserStatusRS setUserStatus(Long id, boolean active);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
}
