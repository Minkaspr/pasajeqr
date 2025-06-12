package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.driver.request.DriverRegisterRequest;
import com.mk.pasajeqr.driver.request.DriverUpdateRequest;
import com.mk.pasajeqr.driver.response.BulkDeleteResponseDTO;
import com.mk.pasajeqr.driver.response.DriverDetailDTO;
import com.mk.pasajeqr.driver.response.DriversResponseDTO;
import com.mk.pasajeqr.user.dto.UserStatusResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {
    DriverDetailDTO createDriver (DriverRegisterRequest request);
    DriverDetailDTO getById(Long id);
    DriversResponseDTO listDrivers(Pageable pageable);
    DriverDetailDTO  updateDriver(Long id, DriverUpdateRequest request);
    UserStatusResponse setUserStatus(Long id, boolean active);
    void deleteDriver(Long id);
    BulkDeleteResponseDTO deleteDrivers(List<Long> ids);
}
