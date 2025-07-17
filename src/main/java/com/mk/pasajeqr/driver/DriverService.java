package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.driver.request.DriverCreateRQ;
import com.mk.pasajeqr.driver.request.DriverUpdateRQ;
import com.mk.pasajeqr.driver.response.*;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.UserStatusRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {
    DriversRS<DriverUserItemRS> listDrivers(Pageable pageable);
    DriversRS<AvailableDriverRS> listAvailableDrivers(Pageable pageable);
    DriverDetailRS createDriver (DriverCreateRQ request);
    DriverDetailRS getById(Long id);
    DriverDetailRS updateDriver(Long id, DriverUpdateRQ request);
    void changePassword(Long id, ChangePasswordRQ request);
    UserStatusRS setUserStatus(Long id, boolean active);
    void deleteDriver(Long id);
    BulkDeleteRS deleteDrivers(List<Long> ids);
    List<DriverStatusCountRS> getDriverStatusSummary();
}
