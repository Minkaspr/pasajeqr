package com.mk.pasajeqr.driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
}
