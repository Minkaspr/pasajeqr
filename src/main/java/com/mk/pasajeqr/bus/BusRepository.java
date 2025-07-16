package com.mk.pasajeqr.bus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    boolean existsByPlate(String plate);
    Page<Bus> findByPlateContainingIgnoreCase(String plate, Pageable pageable);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
