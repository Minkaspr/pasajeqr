package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.utils.DriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
    Page<Driver> findByStatusAndUser_Status(DriverStatus status, Boolean userStatus, Pageable pageable);
}
