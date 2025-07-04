package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.driver.request.DriverCreateRQ;
import com.mk.pasajeqr.driver.request.DriverUpdateRQ;
import com.mk.pasajeqr.driver.response.DriverDetailRS;
import com.mk.pasajeqr.driver.response.DriversRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.UserStatusRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {
    DriverDetailRS createDriver (DriverCreateRQ request);
    DriverDetailRS getById(Long id);
    DriversRS listDrivers(Pageable pageable);
    DriverDetailRS updateDriver(Long id, DriverUpdateRQ request);
    void changePassword(Long id, ChangePasswordRQ request);
    UserStatusRS setUserStatus(Long id, boolean active);
    void deleteDriver(Long id);
    BulkDeleteRS deleteDrivers(List<Long> ids);
}
