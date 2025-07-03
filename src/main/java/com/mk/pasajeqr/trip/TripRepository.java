package com.mk.pasajeqr.trip;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    boolean existsByCode(String code);
    Page<Trip> findByCodeContainingIgnoreCase(String code, Pageable pageable);
}
